import java.util.HashMap;

public class Machine {
	private int[] s;
	private int sp;
	private int pointer;
	private HashMap<String, Integer> table;
	private HashMap<Integer, String> pointers;
	private HashMap<String, Integer> labels;
	
	public Machine(int size) {
		s = new int[size];
		sp = -1;
		pointer = 0;
		table = new HashMap<String, Integer>();
		pointers = new HashMap<Integer, String>();
		labels = new HashMap<String, Integer>();
	}
	
	public int pop() {
		return s[sp--];
	}
	
	public void push(int v) {
		s[++sp] = v;
	}
	
	public void copy() {
		s[sp+1] = s[sp];
		sp++;
	}
	
	public void rvalue(String id) {
		push(table.get(id));
	}
	
	public void lvalue(String id) {
		push(pointer);
		pointers.put(pointer++, id);
	}
	
	public String getLValue(int p) {
		return pointers.get(p);
	}
	
	public void assign(int p, int val) {
		table.put(getLValue(p), val);
	}
	
	public void print() {
		System.out.print("\t{");
		for(int i = 0; i <= sp; i++)
		{
			System.out.print(s[i] + " " );
		}
		System.out.println("}");
	}

	public void label(String l, int index) {
		labels.put(l, index); 
	}
	
	public int getLabel(String l) {
		return labels.get(l); 
	}
}

