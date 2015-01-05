/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package gui.lsystem;

import grammar.lsystem.Expander;
import grammar.lsystem.LSystem;
import gui.ImageDisplayComponent;
import gui.transform.Matrix;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The L-system display pane has the interface to display an L-system.
 * 
 * @author Thomas Finley
 */

public class DisplayPane extends JPanel {
	/**
	 * Implements a display pane.
	 * 
	 * @param lsystem
	 *            the L-system to display
	 */
	public DisplayPane(LSystem lsystem) {
		super(new BorderLayout());
		this.lsystem = lsystem;

		expander = new Expander(lsystem);
		// We can't edit the expansion, of course.
		expansionDisplay.setEditable(false);
		// The user has to be able to change the recursion depth.
		JSpinner spinner = new JSpinner(spinnerModel);
		spinner.setName("derivation");
		spinnerModel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
			}
		});
		// Now, for the angle at which the damn thing is viewed...
		JSpinner s1 = new JSpinner(pitchModel), s2 = new JSpinner(rollModel), s3 = new JSpinner(
				yawModel);
		ChangeListener c = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDisplay();
				// displayAction.setEnabled(true);
			}
		};
		pitchModel.addChangeListener(c);
		rollModel.addChangeListener(c);
		yawModel.addChangeListener(c);
		
		// Lay out the component.
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(spinner, BorderLayout.EAST);
		topPanel.add(expansionDisplay, BorderLayout.CENTER);
		topPanel.add(progressBar, BorderLayout.WEST);
		add(topPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(new JLabel("Pitch"));
		bottomPanel.add(s1);
		bottomPanel.add(new JLabel("Roll"));
		bottomPanel.add(s2);
		bottomPanel.add(new JLabel("Yaw"));
		bottomPanel.add(s3);
		//bottomPanel.setBackground(Color.WHITE);
		imageDisplay.setName("image");
		JScrollPane scroller = new JScrollPane(imageDisplay);
		add(scroller, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		// Finally, set the initial display.
		updateDisplay();
	}

	/**
	 * Updates the display.Graphics2D;
	 */
	private void updateDisplay() {
		if (!optionalPreviousRenderer.map(renderer -> {
			if (!(renderer.isDone() || renderer.cancel(true))) {
				try {
					renderer.get();
				} catch (CancellationException ignored) {
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return false;
				} catch (ExecutionException e) {
					Throwable cause = e.getCause();
					if (cause instanceof RuntimeException) throw (RuntimeException) cause;
					if (cause instanceof Error) throw (Error) cause;
					throw new AssertionError(cause);
				}
			}
			return true;
		}).orElse(true)) return;

		int recursionDepth = spinnerModel.getNumber().intValue();
		final List expansion = expander.expansionForLevel(recursionDepth);
		progressBar.setMaximum(expansion.size() * 2);
		progressBar.setValue(0);
		imageDisplay.setIcon(null);

		if (expansion.size() < 70) {
			String expansionString = LSystemInputPane
				.listAsString(expansion);
			expansionDisplay.setText(expansionString);
		} else
			expansionDisplay.setText("Suffice to say, quite long.");
		// Now, set the display.
		Map parameters = lsystem.getValues();

		Matrix m = new Matrix();
		double pitch = pitchModel.getNumber().doubleValue(), roll = rollModel
			.getNumber().doubleValue(), yaw = yawModel.getNumber()
			.doubleValue();
		m.pitch(pitch);
		m.roll(roll);
		m.yaw(yaw);

		Renderer renderer = new Renderer(expansion, parameters, m) {
			@Override
			protected Graphics2D createGraphics(Rectangle2D bounds) {
				image = new BufferedImage((int) bounds.getWidth() + 10,
					(int) bounds.getHeight() + 10,
					BufferedImage.TYPE_INT_ARGB);
				SwingUtilities.invokeLater(() -> {
					if (isCancelled()) return;
					imageIcon.setImage(image);
					imageDisplay.setIcon(imageIcon);
				});
				Graphics2D graphics = image.createGraphics();
				graphics.translate(-bounds.getX() + 5.0, -bounds.getY() + 5.0);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
				return graphics;
			}

			@Override
			protected void updateProgress(int progress) {
				imageDisplay.repaint();
				progressBar.setValue(progress);
			}
		};
		optionalPreviousRenderer = Optional.of(renderer);
		renderer.execute();
	}

	/**
	 * Prints the current displayed L-system.
	 *
	 * @param g
	 *            the graphics interface for the printer device
	 */
	protected void printComponent(Graphics g) {
		optionalPreviousRenderer.ifPresent(renderer -> {
			if (!renderer.isDone()) {
				try {
					renderer.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				} catch (ExecutionException e) {
					Throwable cause = e.getCause();
					if (cause instanceof RuntimeException) throw (RuntimeException) cause;
					if (cause instanceof Error) throw (Error) cause;
					throw new RuntimeException(cause);
				}
				// Calling get on the EDT delays all EDT-related events from the worker.
				imageIcon.setImage(image);
				imageDisplay.setIcon(imageIcon);
				// imageDisplay implicitly revalidates, which invalidates imageDisplay
				// then asynchronously requests validating the validate-*root*.
				imageDisplay.invalidate();
				imageDisplay.getRootPane().validate();
			}
			imageDisplay.print(g);
		});
	}

	/**
	 * Children are not painted here.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	protected void printChildren(Graphics g) {

	}

	/** The L-system we are displaying here. */
	private LSystem lsystem;

	/** The current expander. */
	private Expander expander = null;

	private final ImageIcon imageIcon = new ImageIcon();

	/** The image display component. */
	private final JLabel imageDisplay = new JLabel(imageIcon, JLabel.CENTER);

	private BufferedImage image;

	/** The spinner model. */
	private SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 200,
			1);

	/** The text field which displays the expansion. */
	private JTextField expansionDisplay = new JTextField();

	/** The progress indicator. */
	private JProgressBar progressBar = new JProgressBar(0, 1);

	/** The action for redisplaying. */
	private Action displayAction = new AbstractAction("Redisplay") {
		public void actionPerformed(ActionEvent e) {
			updateDisplay();
			displayAction.setEnabled(false);
		}
	};

	/** The spinner models for the transforms. */
	private SpinnerNumberModel pitchModel = new SpinnerNumberModel(0, 0, 359,
			15), rollModel = new SpinnerNumberModel(0, 0, 359, 15),
			yawModel = new SpinnerNumberModel(0, 0, 359, 15);

	private Optional<Renderer> optionalPreviousRenderer = Optional.empty();
}
