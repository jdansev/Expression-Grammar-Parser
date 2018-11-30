# LL1 Table-Driven Parser

### Grammar:
```
S → L
L → IX
X → L | ε
I → A | C | W
A → uid := E ;
C → if E then L O fi
O → else L | ε
W → while E do L done
E → E2 Y
Y → Op1 E2 Y | ε
E2 → T Z
Z → Op2 E2 | ε
T → c | uid
Op1 → < | = | !=
Op2 → + | -
```

### Adding or Modifying Rules

To make the modification or addition of grammar rules more simple, a list of static String arrays are declared at the top of the class to store the values for a rule’s first and follow sets. The following code refers to the rule for the start symbol S → L.
```
// S -> L for {uid, if, while}
static List<String> _S_to_L = Arrays.asList("uid", "if", "while");
```

Once the first and follow sets are updated, the corresponding if statement in the parse method should also be updated with the variable for the rule, as well as the string of variables and terminals to push onto the derivation stack.
```
// S -> L for {uid, if, while}
if (der.equals("S") && _S_to_L.contains(str)) {
  parseString.pop();
  parseString.push("L");
}
```

### Usage
```
javac LL1Parser.java
java LL1Parser uid.txt
```
