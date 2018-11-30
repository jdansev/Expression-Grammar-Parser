import java.util.NoSuchElementException;
import java.io.FileNotFoundException;
import java.util.EmptyStackException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;

public class LL1Parser {

	// S -> L  for {uid, if, while}
	static List<String> _S_to_L = Arrays.asList("uid", "if", "while");
	// L -> I X  for {uid, if, while}
	static List<String> _L_to_IX = Arrays.asList("uid", "if", "while");
	// X -> L  for {uid, if, while}
	static List<String> _X_to_L = Arrays.asList("uid", "if", "while");
	// X -> epsilon  for {$,done,else,fi}
	static List<String> _X_to_Epsilon = Arrays.asList("$", "done", "else", "fi");
	// I -> A  for {uid}
	static List<String> _I_to_A = Arrays.asList("uid");
	// I -> C  for {if}
	static List<String> _I_to_C = Arrays.asList("if");
	// I -> W  for {while}
	static List<String> _I_to_W = Arrays.asList("while");
	// A -> uid := E ;  for {uid}
	static List<String> _A_to_uidRule = Arrays.asList("uid");
	// C -> if E then L O fi  for {if}
	static List<String> _C_to_ifRule = Arrays.asList("if");
	// O -> epsilon  for {fi}
	static List<String> _O_to_epsilon = Arrays.asList("fi");
	// O -> else L  for {else}
	static List<String> _O_to_elseRule = Arrays.asList("else");
	// W -> while E do L done  for {while}
	static List<String> _W_to_whileRule = Arrays.asList("while");
	// E -> E2 Y  for {uid, c}
	static List<String> _E_to_E2Y = Arrays.asList("uid", "c");
	// Y -> Op1 E2 Z  for {<, =, !=}
	static List<String> _Y_to_Op1E2Z = Arrays.asList("<", "=", "!=");
	// Y -> epsilon  for {;,then,do}
	static List<String> _Y_to_epsilon = Arrays.asList(";", "then", "do");
	// E2 -> T Z  for {uid, c}
	static List<String> _E2_to_TZ = Arrays.asList("uid", "c");
	// Z -> Op2 E2  for {+, -}
	static List<String> _Z_to_Op2E2 = Arrays.asList("+", "-");
	// Z -> epsilon  for {<,=,!=,;,then,do}
	static List<String> _Z_to_epsilon = Arrays.asList("<", "=", "!=", ";", "then", "do");
	// T -> uid  for {uid}
	static List<String> _T_to_uid = Arrays.asList("uid");
	// T -> c  for {c}
	static List<String> _T_to_c = Arrays.asList("c");
	// Op1 -> <  for {<}
	static List<String> _Op1_to_lessThan = Arrays.asList("<");
	// Op1 -> =  for {=}
	static List<String> _Op1_to_equals = Arrays.asList("=");
	// Op1 -> !=  for {!=}
	static List<String> _Op1_to_notEquals = Arrays.asList("!=");
	// Op2 -> + for {+}
	static List<String> _Op2_to_plus = Arrays.asList("+");
	// Op1 -> -  for {-}
	static List<String> _Op2_to_minus = Arrays.asList("-");


	static ArrayList<String> terminals = new ArrayList<String>();
	static Scanner scan;
	static BasicQueue<Character> q = new BasicQueue<Character>();
	static BasicQueue<String> convertedInput = new BasicQueue<String>();
	static boolean printErrors = false;

	public static void main(String[] args) {

		// Initialize terminals array list.
		terminals.add("uid");
		terminals.add(":=");
		terminals.add(";");
		terminals.add("if");
		terminals.add("then");
		terminals.add("fi");
		terminals.add("else");
		terminals.add("while");
		terminals.add("do");
		terminals.add("done");
		terminals.add("c");
		terminals.add("<");
		terminals.add("=");
		terminals.add("!=");
		terminals.add("+");
		terminals.add("-");

		// If args length is not 1 or 2, quit program.
		if (args.length != 1 && args.length != 2) {
			System.err.println("Invalid file name argument. System terminating.");
			System.exit(0);
		}

		// If -e flag is second argument, turn on print errors.
		if (args.length == 2 && args[1].equals("-e")) {
			printErrors = true;
		}

		// First argument is filepath.
		File filePath = new File(args[0]);

		// Check if file exists.
		try {
			scan = new Scanner(filePath);
		} catch (FileNotFoundException e) {
			System.out.println("File not found. System terminating.");
			System.exit(0);
		}

		// Scan file by character, ignoring whitespace.
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			for (int i = 0; i < line.length(); i++) {
				char ch = line.charAt(i);
				if (ch != ' ' && ch != '\n' && ch != '\t') {
					q.enqueue(ch);
				}
			}

		}

		// Parses the input variables one character at a time, adds terminal to the queue.
		while (!q.isEmpty()) {
			char ch = q.front();
			switch (ch) {
				case 'u': terminal_uid(); break;
				case ':': terminal_assignment(); break;
				case ';': terminal_semi_colon(); break;
				case 'i': terminal_if(); break;
				case 't': terminal_then(); break;
				case 'f': terminal_fi(); break;
				case 'e': terminal_else(); break;
				case 'w': terminal_while(); break;
				case 'd': terminal_do_done(); break;
				case 'c': terminal_c(); break;
				case '<': terminal_less_than(); break;
				case '=': terminal_equals(); break;
				case '!': terminal_not_equal(); break;
				case '+': terminal_plus(); break;
				case '-': terminal_minus(); break;
				case '$': terminal_dollar_sign(); break;
				default: string_rejected(); // On foreign characters, reject entire string.	
			}
		}

		parse();

	}

	/*

	The following methods check if characters read from the text file are terminal symbols.
	If so, add the symbol to a queue which will be used for derivations in the next step.

	*/

	private static void terminal_uid() {
		if (q.dequeue() == 'u'&&
			q.dequeue() == 'i' &&
			q.dequeue() == 'd') {
			convertedInput.enqueue("uid");
		} else {
			string_rejected();
		}
	}

	private static void terminal_assignment() {
		if (q.dequeue() == ':'&&
			q.dequeue() == '=') {
			convertedInput.enqueue(":=");
		} else {
			string_rejected();
		}
	}

	private static void terminal_semi_colon() {
		if (q.dequeue() == ';') {
			convertedInput.enqueue(";");
		} else {
			string_rejected();
		}
	}

	private static void terminal_if() {
		if (q.dequeue() == 'i' &&
			q.dequeue() == 'f') {
			convertedInput.enqueue("if");
		} else {
			string_rejected();
		}
	}

	private static void terminal_then() {
		if (q.dequeue() == 't' &&
			q.dequeue() == 'h' &&
			q.dequeue() == 'e'&&
			q.dequeue() == 'n') {
			convertedInput.enqueue("then");
		} else {
			string_rejected();
		}
	}

	private static void terminal_fi() {
		if (q.dequeue() == 'f' &&
			q.dequeue() == 'i') {
			convertedInput.enqueue("fi");
		} else {
			string_rejected();
		}
	}

	private static void terminal_else() {
		if (q.dequeue() == 'e' &&
			q.dequeue() == 'l' &&
			q.dequeue() == 's' &&
			q.dequeue() == 'e') {
			convertedInput.enqueue("else");
		} else {
			string_rejected();
		}
	}

	private static void terminal_while() {
		if (q.dequeue() == 'w' &&
			q.dequeue() == 'h' &&
			q.dequeue() == 'i' &&
			q.dequeue() == 'l' &&
			q.dequeue() == 'e') {
			convertedInput.enqueue("while");
		} else {
			string_rejected();
		}
	}

	private static void terminal_do_done() {
		if (q.dequeue() == 'd' &&
			q.dequeue() == 'o' && (q.isEmpty() || 
			q.front() != 'n')) {
			convertedInput.enqueue("do");
		} else if (q.dequeue() == 'n' &&
			q.dequeue() == 'e') {
			convertedInput.enqueue("done");
		} else {
			string_rejected();
		}
	}

	private static void terminal_c() {
		if (q.dequeue() == 'c') {
			convertedInput.enqueue("c");
		} else {
			string_rejected();
		}
	}

	private static void terminal_less_than() {
		if (q.dequeue() == '<') {
			convertedInput.enqueue("<");
		} else {
			string_rejected();
		}
	}

	private static void terminal_equals() {
		if (q.dequeue() == '=') {
			convertedInput.enqueue("=");
		} else {
			string_rejected();
		}
	}

	private static void terminal_not_equal() {
		if (q.dequeue() == '!' && q.dequeue() == '=') {
			convertedInput.enqueue("!=");
		} else {
			string_rejected();
		}
	}

	private static void terminal_plus() {
		if (q.dequeue() == '+') {
			convertedInput.enqueue("+");
		} else {
			string_rejected();
		}
	}

	private static void terminal_minus() {
		if (q.dequeue() == '-') {
			convertedInput.enqueue("-");
		} else {
			string_rejected();
		}
	}

	private static void terminal_dollar_sign() {
		if (q.dequeue() == '$') {
			convertedInput.enqueue("$");
		} else {
			string_rejected();
		}
	}

	// Reject the string.
	private static void string_rejected() {
		System.out.println("REJECTED");
		System.exit(0);
	}


	private static void parse() {

		// The end of the converted input is marked with a $.
		convertedInput.enqueue("$");

		// Holds a stack containing the derivation symbols.
		BasicStack<String> parseString = new BasicStack<String>();

		// Initializes the queue with a dollar sign.
		parseString.push("$");
		parseString.list();
		parseString.push("S");

		while (!parseString.top().equals("$")) {

			// Front symbol on the queue of converted input.
			String str = convertedInput.front();
			// Top symbol on the stack of the derivation string.
			String der = parseString.top();

			// List the contents of the parse string.
			parseString.list();

			// Grammar rules for G'.

			// S -> L  for {uid, if, while}
			if (der.equals("S") && _S_to_L.contains(str)) {
				parseString.pop();
				parseString.push("L");
			}

			// L -> I X  for {uid, if, while}
			else if (der.equals("L") && _L_to_IX.contains(str)) {
				parseString.pop();
				parseString.push("X");
				parseString.push("I");
			}

			// X -> L  for {uid, if, while}
			else if (der.equals("X") && _X_to_L.contains(str)) {
				parseString.pop();
				parseString.push("L");
			}

			// X -> epsilon  for {$,done,else,fi}
			else if (der.equals("X") && _X_to_Epsilon.contains(str)) {
				parseString.pop();
			}

			// I -> A  for {uid}
			else if (der.equals("I") && _I_to_A.contains(str)) {
				parseString.pop();
				parseString.push("A");
			}

			// I -> C  for {if}
			else if (der.equals("I") && _I_to_C.contains(str)) {
				parseString.pop();
				parseString.push("C");
			}

			// I -> W  for {while}
			else if (der.equals("I") && _I_to_W.contains(str)) {
				parseString.pop();
				parseString.push("W");
			}

			// A -> uid := E ;  for {uid}
			else if (der.equals("A") && _A_to_uidRule.contains(str)) {
				parseString.pop();
				parseString.push(";");
				parseString.push("E");
				parseString.push(":=");
				parseString.push("uid");
			}

			// C -> if E then L O fi  for {if}
			else if (der.equals("C") && _C_to_ifRule.contains(str)) {
				parseString.pop();
				parseString.push("fi");
				parseString.push("O");
				parseString.push("L");
				parseString.push("then");
				parseString.push("E");
				parseString.push("if");
			}

			// O -> epsilon  for {fi}
			else if (der.equals("O") && _O_to_epsilon.contains(str)) {
				parseString.pop();
			}

			// O -> else L  for {else}
			else if (der.equals("O") && _O_to_elseRule.contains(str)) {
				parseString.pop();
				parseString.push("L");
				parseString.push("else");
			}

			// W -> while E do L done  for {while}
			else if (der.equals("W") && _W_to_whileRule.contains(str)) {
				parseString.pop();
				parseString.push("done");
				parseString.push("L");
				parseString.push("do");
				parseString.push("E");
				parseString.push("while");
			}

			// E -> E2 Y  for {uid, c}
			else if (der.equals("E") && _E_to_E2Y.contains(str)) {
				parseString.pop();
				parseString.push("Y");
				parseString.push("E2");
			}

			// Y -> Op1 E2 Z  for {<, =, !=}
			else if (der.equals("Y") && _Y_to_Op1E2Z.contains(str)) {
				parseString.pop();
				parseString.push("Z");
				parseString.push("E2");
				parseString.push("Op1");
			}

			// Y -> epsilon  for {;,then,do}
			else if (der.equals("Y") && _Y_to_epsilon.contains(str)) {
				parseString.pop();
			}

			// E2 -> T Z  for {uid, c}
			else if (der.equals("E2") && _E2_to_TZ.contains(str)) {
				parseString.pop();
				parseString.push("Z");
				parseString.push("T");
			} else if (der.equals("E2") && !_E2_to_TZ.contains(str)) {
				String received = convertedInput.front();
				System.out.printf("Expected symbol 'uid' or 'c' instead of '%s'\nREJECTED\n", received);
				break;
			}

			// Z -> Op2 E2  for {+, -}
			else if (der.equals("Z") && _Z_to_Op2E2.contains(str)) {
				parseString.pop();
				parseString.push("E2");
				parseString.push("Op2");
			}

			// Z -> epsilon  for {<,=,!=,;,then,do}
			else if (der.equals("Z") && _Z_to_epsilon.contains(str)) {
				parseString.pop();
			}

			// T -> uid  for {uid}
			else if (der.equals("T") && _T_to_uid.contains(str)) {
				parseString.pop();
				parseString.push("uid");
			}

			// T -> c  for {c}
			else if (der.equals("T") && _T_to_c.contains(str)) {
				parseString.pop();
				parseString.push("c");
			}

			else if (der.equals("T") && !(_T_to_uid.contains(str) || _T_to_c.contains(str))) {
				String received = convertedInput.front();
				System.out.printf("Expected symbol 'uid' or 'c' instead of '%s'\nREJECTED\n", received);
				break;
			}

			// Op1 -> <  for {<}
			else if (der.equals("Op1") && _Op1_to_lessThan.contains(str)) {
				parseString.pop();
				parseString.push("<");
			}

			// Op1 -> =  for {=}
			else if (der.equals("Op1") && _Op1_to_equals.contains(str)) {
				parseString.pop();
				parseString.push("=");
			}

			// Op1 -> !=  for {!=}
			else if (der.equals("Op1") && _Op1_to_notEquals.contains(str)) {
				parseString.pop();
				parseString.push("!=");
			}

			else if (der.equals("Op1") && !(_Op1_to_lessThan.contains(str) || _Op1_to_equals.contains(str) || _Op1_to_notEquals.contains(str))) {
				String received = convertedInput.front();
				System.out.printf("Expected symbol '<' or '=' or '!=' instead of '%s'\nREJECTED\n", received);
				break;
			}

			// Op2 -> + for {+}
			else if (der.equals("Op2") && _Op2_to_plus.contains(str)) {
				parseString.pop();
				parseString.push("+");
			}

			// Op1 -> -  for {-}
			else if (der.equals("Op2") && _Op2_to_minus.contains(str)) {
				parseString.pop();
				parseString.push("-");
			}

			else if (der.equals(str)) {
				parseString.pop();
				convertedInput.dequeue();
			}

			// ERROR Handling:
			// Reject the string if a der terminal and a str terminal don't match.
			else if (!der.equals(str) && terminals.contains(der) && terminals.contains(str)) {
				String expected = parseString.top();
				String received = convertedInput.front();
				System.out.printf("Expected symbol '%s' instead of '%s'\n", expected, received);
				string_rejected();
				break;
			}

			else {
				string_rejected();
			}

			// If both derived and string have been parsed to $, accept the string and exit loop.
			if (parseString.top().equals("$") && convertedInput.front().equals("$")) {
				parseString.list();
				System.out.println("ACCEPTED");
				break;
			}
			
		}

	}

}

// Basic Queue First-In-Last-Out retrieval of data.
class BasicQueue<E>{
	private ArrayList<E> items;
	public BasicQueue() {
		items = new ArrayList<E>();
	}
	public int size() {
		return items.size();
	}
	public boolean isEmpty() {
		return (size() == 0) ? true : false;
	}
	public E front() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return items.get(0);
	}
	public void enqueue(E element) {
		items.add(element);
	}
	public E dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return items.remove(0);
	}
}

// Basic Queue First-In-First-Out retrieval of data.
class BasicStack<E> {
	private ArrayList<E> items;
	public BasicStack () {
		items = new ArrayList<E>();
	}
	public int size() {
		return items.size();
	}
    public boolean isEmpty() {
    	return items.isEmpty();
    }
    public E top() {
    	if (items.size() == 0) {
    		throw new EmptyStackException();
    	} else {
    		return items.get(items.size()-1);
    	}
    }
    public void push(E element) {
    	items.add(element);
    }
    public E pop() {
    	if (items.size() == 0) {
    		throw new EmptyStackException();
    	}
    	return items.remove(items.size()-1);
    }
    public void list() {
    	for (int i = size()-1; i >= 0; i--) {
    		System.out.print(items.get(i) + " ");
    	}
    	System.out.println();
    }
}
