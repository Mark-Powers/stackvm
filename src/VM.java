import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class VM {
	static int pc;
	static String line;

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Must pass in file name!");
			System.exit(1);
		}
		boolean debug = false;
		if (args.length == 2) {
			debug = true;
		}

		File source = new File(args[0]);
		try {
			BufferedReader br = new BufferedReader(new FileReader(source));

			Machine stack = new Machine(100);

			ArrayList<String> lines = new ArrayList<String>();
			String currline = br.readLine();
			int index = 0;
			while (currline != null) {
				lines.add(currline);
				if (currline.startsWith("label")) {
					// -1 since the PC is loaded then incremented
					stack.label(currline.split(" ")[1], index - 1);
				}
				index++;
				currline = br.readLine();
			}
			br.close();

			for (pc = 0; pc < lines.size(); pc++) {
				line = lines.get(pc);
				if (line.startsWith("halt")) {
					break;
				} else if (line.startsWith("printstr")) {
					char p;
					while( (p = (char) stack.pop()) != '\0') {
						System.out.print(p);
					}
					System.out.println();
				} else if (line.startsWith("print")) {
					System.out.println(stack.pop());
				} else if (line.startsWith("rvalue")) {
					stack.rvalue(line.split(" ")[1]);
				} else if (line.startsWith("lvalue")) {
					stack.lvalue(line.split(" ")[1]);
				} else if (line.startsWith("push")) {
					stack.push(Integer.parseInt(line.split(" ")[1]));
				} else if (line.startsWith("pop")) {
					stack.pop();
				} else if (line.startsWith(":=")) {
					int value = stack.pop();
					stack.assign(stack.pop(), value);
				} else if (line.startsWith("+")) {
					stack.push(stack.pop() + stack.pop());
				} else if (line.startsWith("-")) {
					int tmp = stack.pop();
					stack.push(stack.pop() - tmp);
				} else if (line.startsWith("/")) {
					int tmp = stack.pop();
					stack.push(stack.pop() / tmp);
				} else if (line.startsWith("%")) {
					int tmp = stack.pop();
					stack.push(stack.pop() % tmp);
				} else if (line.startsWith("*")) {
					stack.push(stack.pop() * stack.pop());
				} else if (line.startsWith("label")) {
					continue;
				} else if (line.startsWith("goto")) {
					pc = stack.getLabel(line.split(" ")[1]);
				} else if (line.startsWith("copy")) {
					stack.copy();
				} else if (line.startsWith("gofalse")) {
					if (stack.pop() == 0) {
						pc = stack.getLabel(line.split(" ")[1]);
					}
				} else if (line.startsWith("gotrue")) {
					if (stack.pop() != 0) {
						pc = stack.getLabel(line.split(" ")[1]);
					}
				} else if (line.startsWith("gogt")) {
					if (stack.pop() < 0) {
						pc = stack.getLabel(line.split(" ")[1]);
					}
				} else if (line.startsWith("gogte")) {
					if (stack.pop() < 0) {
						pc = stack.getLabel(line.split(" ")[1]);
					}
				} else if (line.startsWith("not")) {
					if (stack.pop() == 0) {
						stack.push(1);
					} else {
						stack.push(0);
					}
				}

				if (debug) {
					System.out.println("\t" + line);
					stack.print();
				}
			}
		} catch (Exception e) {
			System.err.println("Error: PC=" + pc);
			System.err.println(line);
			try {
				throw e;
			} catch(ArrayIndexOutOfBoundsException e2){
				System.err.println("Stack empty!");
			} catch(Exception e2) {
				e.printStackTrace();
			}
		}
	}
}
