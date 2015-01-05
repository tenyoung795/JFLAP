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

import gui.transform.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 * A <CODE>Renderer</CODE> object allows a client to create an image of a
 * string of symbols generated, presumably, from an <CODE>LSystem</CODE>.
 * <P>
 *
 * The following symbols have significance:
 *
 * @see grammar.lsystem.Expander
 * @see grammar.lsystem.LSystem
 *
 * @author Thomas Finley
 */

public abstract class Renderer extends SwingWorker<Void, Integer> {
	/**
	 * Instantiates a renderer object.
	 *
	 * @param symbols
	 *            a list of symbols
	 * @param parameters
	 *            the parameters
	 * @param matrix
	 *            the initial transform matrix for the turtle
	 * @throws IllegalArgumentException
	 *            if there is a passed in graphics object and its clip area is
	 *            not set
	 */
	public Renderer(List<String> symbols, Map<String, String> parameters, Matrix matrix) {
		this.symbols = new ArrayList<>(symbols);
		this.parameters = new HashMap<>(parameters);
		this.matrix = new Matrix(matrix);

		optionalSymbolHandlers.put("g", new MoveHandler(true, true));
		optionalSymbolHandlers.put("f", new MoveHandler(false, true));
		optionalSymbolHandlers.put("+", new TurnHandler(true));
		optionalSymbolHandlers.put("-", new TurnHandler(false));
		optionalSymbolHandlers.put("&", new PitchHandler(true));
		optionalSymbolHandlers.put("^", new PitchHandler(false));
		optionalSymbolHandlers.put("/", new RollHandler(true));
		optionalSymbolHandlers.put("*", new RollHandler(false));
		optionalSymbolHandlers.put("[", new PushTurtleHandler());
		optionalSymbolHandlers.put("]", new PopTurtleHandler());
		optionalSymbolHandlers.put("!", new WidthChangeHandler(true));
		optionalSymbolHandlers.put("~", new WidthChangeHandler(false));
		optionalSymbolHandlers.put("{", new BeginPolygonHandler());
		optionalSymbolHandlers.put("}", new ClosePolygonHandler());
		optionalSymbolHandlers.put("%", new ReverseHandler());
		optionalSymbolHandlers.put("#", new HueChangeHandler(false, true));
		optionalSymbolHandlers.put("@", new HueChangeHandler(false, false));
		optionalSymbolHandlers.put("##", new HueChangeHandler(true, true));
		optionalSymbolHandlers.put("@@", new HueChangeHandler(true, false));

		handlers.putAll(optionalSymbolHandlers);
		handlers.put("color", new DrawColorHandler());
		handlers.put("polygonColor", new PolygonColorHandler());
		CommandHandler angleIncrement = new AngleIncrementHandler();
		handlers.put("angle", angleIncrement);
		handlers.put("angleIncrement", angleIncrement);
		handlers.put("lineWidth", new LineWidthHandler());
		handlers.put("lineIncrement", new LineWidthIncrementHandler());
		handlers.put("distance", new DistanceHandler());
		handlers.put("hueChange", new HueAngleIncrementHandler());
	}

	protected abstract Graphics2D createGraphics(Rectangle2D bounds);

	@Override
	protected final void process(List<Integer> chunks) {
		if (isCancelled()) return;
		updateProgress(chunks.get(chunks.size() - 1));
	}

	protected abstract void updateProgress(int progress);

	@Override
	protected final void done() {
		try {
			get();
		} catch (CancellationException ignored) {
			return;
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException) throw (RuntimeException) cause;
			if (cause instanceof Error) throw (Error) cause;
			throw new AssertionError(cause);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Returns the command handler for a symbol.
	 *
	 * @param symbol
	 *            the symbol
	 * @return the command handler for that symbol, or <CODE>Optional.empty()</CODE> if no
	 *         handler exists
	 */
	private Optional<CommandHandler> getHandler(String symbol) {
		return Optional.ofNullable(handlers.get(symbol));
	}

	private Optional<OptionalSymbolCommandHandler> getOptionalSymbolHandler(String symbol) {
		return Optional.ofNullable(optionalSymbolHandlers.get(symbol));
	}

	/**
	 * Does an assignment from a key to a value, calling the handler as well as
	 * setting the value in the turtle.
	 *
	 * @param graphicsOptional
	 *            the optional graphics
	 * @param key
	 *            the key
	 * @param value
	 *            the value, possibly a mathematical expression
	 */
	private void assign(Optional<Graphics2D> graphicsOptional, String key, String value) {
		if (!NONASSIGN_WORDS.contains(key)) {
			currentTurtle.assign(key, value);
			value = currentTurtle.get(key).toString();
		}
		Optional<CommandHandler> handler = getHandler(key);
		String finalValue = value;
		handler.ifPresent(h -> h.handle(graphicsOptional, finalValue));
	}

	/**
	 * Given a list of symbols and a dictionary of parameters, this will render
	 * a representation of those symbols to a graphics.
	 */
	@Override
	protected final Void doInBackground() throws Exception {
		Rectangle2D bounds = pass(Optional.empty());
		if (isCancelled()) return null;
		Graphics2D graphics = createGraphics(bounds);
		try {
			if (isCancelled()) return null;
			pass(Optional.of(graphics));
			return null;
		} finally {
			graphics.dispose();
		}
	}

	private Rectangle2D pass(Optional<Graphics2D> graphicsOptional) {
		turtleStack.clear();
		currentTurtle = new Turtle(matrix);
		// Do the initial parameters.
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			assign(graphicsOptional, entry.getKey(), entry.getValue());
		}
		// Set the initial drawing state.
		graphicsOptional.ifPresent(g -> g.setColor(currentTurtle.getColor()));
		capLinePath(graphicsOptional);
		// Repeatedly read symbols, and call the appropriate
		// command handler.
		int completedSymbols = graphicsOptional.isPresent() ? symbols.size() : 0;
		for (String symbol : symbols) {
			if (isCancelled()) break;

			publish(completedSymbols);
			completedSymbols++;

			Optional<OptionalSymbolCommandHandler> handler = getOptionalSymbolHandler(symbol);
			if (handler.isPresent()) {
				handler.get().handle(graphicsOptional);
				continue;
			}
			// OKAY, perhaps this is an assignment?
			int equalsPosition = symbol.indexOf('=');
			if (equalsPosition != -1) {
				String key = symbol.substring(0, equalsPosition);
				String value = symbol.substring(equalsPosition + 1);
				// Get the assignment.
				assign(graphicsOptional, key, value);
			}
			// Well, let's go on. Perhaps this is a symbol with
			// an argument.
			int leftParenPosition = symbol.indexOf('('), rightParenPosition = symbol
					.lastIndexOf(')');
			if (leftParenPosition != -1 && rightParenPosition != -1
					&& leftParenPosition < rightParenPosition) {
				String key = symbol.substring(0, leftParenPosition);
				String value = symbol.substring(leftParenPosition + 1,
						rightParenPosition);
				getHandler(key).ifPresent(h -> h.handle(graphicsOptional, value));
			}
		}
		publish(completedSymbols);
		capLinePath(graphicsOptional);
		// We pop all the turtle stacks to make sure the bounds
		// are okay...
		while (!turtleStack.isEmpty())
			popTurtleStack(graphicsOptional);
		return currentTurtle.getBounds();
	}

	/**
	 * This will pop the turtle stack.
	 */
	private void popTurtleStack(Optional<Graphics2D> graphicsOptional) {
		Turtle lt = (Turtle) turtleStack.pop();
		lt.updateBounds(currentTurtle);
		currentTurtle = lt;
		graphicsOptional.ifPresent(g -> {
			g.setColor(currentTurtle.getColor());
			g.setStroke(currentTurtle.getStroke());
		});
	}

	/**
	 *
	 */
	private final void capLinePath(Optional<Graphics2D> graphicsOptional) {
		graphicsOptional.ifPresent(g -> g.draw(linePath));
		linePath.reset(); // Clear the path...
		linePath.moveTo((float) currentTurtle.position.getX(),
				(float) currentTurtle.position.getY());
		// We've started anew!
	}

	/** The command handler maps from symbols to the appropriate handler. */
	private Map<String, CommandHandler> handlers = new HashMap<>();
	private Map<String, OptionalSymbolCommandHandler> optionalSymbolHandlers = new HashMap<>();

	/** The stack of turtles. */
	private Stack turtleStack = new Stack();

	/** The current turtle. */
	private Turtle currentTurtle;

	private final List<String> symbols;

	private final Map<String, String> parameters;

	private final Matrix matrix;

	/** The polygon. Null if no polygon is being drawn right now. */
	private GeneralPath polygon = null;

	/** Lines paths. */
	private GeneralPath linePath = new GeneralPath();

	/** The set of words that can be assigned to. */
	public static Set ASSIGN_WORDS;

	/** The set of words that cannot be assigned a numerical value. */
	public static Set NONASSIGN_WORDS;

	static {
		Set s = new TreeSet();
		s.add("color");
		s.add("polygonColor");
		NONASSIGN_WORDS = Collections.unmodifiableSet(new HashSet(s));
		s.add("angle");
		s.add("lineWidth");
		s.add("lineIncrement");
		s.add("distance");
		s.add("hueChange");
		ASSIGN_WORDS = Collections.unmodifiableSet(s);
	}

	// / THE COMMAND HANDLERS!

	/**
	 * This is a command handler. This is the object that responds to the
	 * command.
	 */
	@FunctionalInterface
	protected interface CommandHandler {
		/**
		 * Handles the command.
		 * @param graphicsOptional
		 * @param symbol
		 */
		void handle(Optional<Graphics2D> graphicsOptional, String symbol);
	}

	@FunctionalInterface
	protected interface OptionalSymbolCommandHandler extends CommandHandler {
		void handle(Optional<Graphics2D> graphicsOptional, Optional<String> symbol);

		default void handle(Optional<Graphics2D> graphicsOptional) {
			handle(graphicsOptional, Optional.empty());
		}

		default void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			handle(graphicsOptional, Optional.of(symbol));
		}
	}

	/**
	 * This handles moving the cursor.
	 */
	private class MoveHandler implements OptionalSymbolCommandHandler {
		public MoveHandler(boolean pendown, boolean forward) {
			this.pendown = pendown;
			this.forward = forward;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			// Evaluate if necessary.
			if (!symbolOptional.isPresent())
				currentTurtle.go(forward);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				currentTurtle.go(forward ? d : -d);
			}

			if (!graphicsOptional.isPresent())
				return;
			if (pendown) {
				if (polygon == null) {
					// We're not adding to the polygon!
					linePath.lineTo((float) currentTurtle.position.getX(),
							(float) currentTurtle.position.getY());
				} else {
					// We are adding to the polygon!
					polygon.lineTo((float) currentTurtle.position.getX(),
							(float) currentTurtle.position.getY());
				}
			} else {
				linePath.moveTo((float) currentTurtle.position.getX(),
						(float) currentTurtle.position.getY());
			}

		}

		private boolean pendown;

		private boolean forward;

		private Line2D line = new Line2D.Double();
	}

	/**
	 * This handles turning.
	 */
	private class TurnHandler implements OptionalSymbolCommandHandler {
		public TurnHandler(boolean clockwise) {
			this.clockwise = clockwise;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			// Evaluate if necessary.
			if (!symbolOptional.isPresent())
				currentTurtle.turn(clockwise);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				currentTurtle.turn(clockwise ? -d : d);
			}
		}

		private boolean clockwise;
	}

	/**
	 * This handles pitching.
	 */
	private class PitchHandler implements OptionalSymbolCommandHandler {
		public PitchHandler(boolean down) {
			this.down = down;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			if (!symbolOptional.isPresent())
				currentTurtle.pitch(down);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				currentTurtle.pitch(down ? d : -d);
			}
		}

		private boolean down;
	}

	/**
	 * This handles rolling.
	 */
	private class RollHandler implements OptionalSymbolCommandHandler {
		public RollHandler(boolean right) {
			this.right = right;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			if (!symbolOptional.isPresent())
				currentTurtle.roll(right);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				currentTurtle.roll(right ? -d : d);
			}
		}

		private boolean right;
	}

	/**
	 * This handles pushing on the turtle stack.
	 */
	private class PushTurtleHandler implements OptionalSymbolCommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			turtleStack.push(currentTurtle.clone());
		}
	}

	/**
	 * This handles popping the turtle stack.
	 */
	private class PopTurtleHandler implements OptionalSymbolCommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			capLinePath(graphicsOptional);
			popTurtleStack(graphicsOptional);
			capLinePath(graphicsOptional);
		}
	}

	/**
	 * This handles changing the width of lines.
	 */
	private class WidthChangeHandler implements OptionalSymbolCommandHandler {
		public WidthChangeHandler(boolean increment) {
			this.increment = increment;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			capLinePath(graphicsOptional);
			if (!symbolOptional.isPresent())
				currentTurtle.changeLineWidth(increment);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				currentTurtle.changeLineWidth(increment ? d : -d);
			}
			graphicsOptional.ifPresent(g -> g.setStroke(currentTurtle.getStroke()));
		}

		private boolean increment;
	}

	/**
	 * This handles change of the draw color.
	 */
	private class DrawColorHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			if (!graphicsOptional.isPresent())
				return;
			capLinePath(graphicsOptional);
			currentTurtle.setColor(symbol);
			graphicsOptional.ifPresent(g -> g.setColor(currentTurtle.getColor()));
		}
	}

	/**
	 * This handles change of the polygon color.
	 */
	private class PolygonColorHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			if (!graphicsOptional.isPresent())
				return;
			currentTurtle.setPolygonColor(symbol);
		}
	}

	/**
	 * This handles change of the angle increment.
	 */
	private class AngleIncrementHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			currentTurtle.setAngleChange(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles change of the line width.
	 */
	private class LineWidthHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			if (!graphicsOptional.isPresent())
				return;
			capLinePath(graphicsOptional);
			currentTurtle.setLineWidth(Double.parseDouble(symbol));
			graphicsOptional.ifPresent(g -> g.setStroke(currentTurtle.getStroke()));
		}
	}

	/**
	 * This handles change of the line width increment.
	 */
	private class LineWidthIncrementHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			currentTurtle.setLineIncrement(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles change of individual line lengths.
	 */
	private class DistanceHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			currentTurtle.distance = Double.parseDouble(symbol);
		}
	}

	/**
	 * This handler begins a polygon.
	 */
	private class BeginPolygonHandler implements OptionalSymbolCommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			if (!graphicsOptional.isPresent() || polygon != null)
				return; // Hrm.
			capLinePath(graphicsOptional);
			polygon = new GeneralPath();
			polygon.moveTo((float) currentTurtle.position.getX(),
					(float) currentTurtle.position.getY());
		}
	}

	/**
	 * This handler closes a polygon.
	 */
	private class ClosePolygonHandler implements OptionalSymbolCommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			if (!graphicsOptional.isPresent())
				return;
			capLinePath(graphicsOptional);
			polygon.closePath();
			graphicsOptional.ifPresent(g -> {
					g.setColor(currentTurtle.polygonColor);
					g.fill(polygon);
					g.setColor(currentTurtle.color);
				});
			polygon = null;
		}
	}

	/**
	 * The reverse handler.
	 */
	private class ReverseHandler implements OptionalSymbolCommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			currentTurtle.turn(180.0);
		}
	}

	/**
	 * This handles change of the hue angle increment.
	 */
	private class HueAngleIncrementHandler implements CommandHandler {
		public final void handle(Optional<Graphics2D> graphicsOptional, String symbol) {
			currentTurtle.setHueChange(Double.parseDouble(symbol));
		}
	}

	/**
	 * This handles changing the hue angle.
	 */
	private class HueChangeHandler implements OptionalSymbolCommandHandler {
		public HueChangeHandler(boolean polygon, boolean add) {
			this.polygon = polygon;
			this.add = add;
		}

		public final void handle(Optional<Graphics2D> graphicsOptional,
			Optional<String> symbolOptional) {
			if (!graphicsOptional.isPresent())
				return;
			capLinePath(graphicsOptional);
			if (!symbolOptional.isPresent())
				if (polygon)
					currentTurtle.changePolygonHue(add);
				else
					currentTurtle.changeHue(add);
			else {
				double d = currentTurtle.valueOf(symbolOptional.get()).doubleValue();
				d = add ? d : -d;
				if (polygon)
					currentTurtle.changePolygonHue(d);
				else
					currentTurtle.changeHue(d);
			}
			graphicsOptional.ifPresent(g -> g.setColor(currentTurtle.getColor()));
		}

		private boolean add, polygon;
	}
}
