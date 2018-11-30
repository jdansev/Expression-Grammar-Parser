# Grammar-Parser


### Grammar G:
```
S → L
L → IX
X → L
X → ε
I → A
I → C
I → W
A → uid := E ;
C → if E then L O fi
O → else L
O → ε
W → while E do L done
E → E2 Y
Y → Op1 E2 Y
Y → ε
E2 → T Z
Z → Op2 E2
Z → ε
T → c
T → uid
Op1 → <
Op1 → =
Op1 → !=
Op2 → +
Op2 → -
```

### First and Follow Sets:
```
FIRST(S) = {uid, if, while}
FIRST(L) = {uid, if, while}
FIRST(X) = {uid, if, while, ε}  FOLLOW(X) = {$, done, else, fi}
FIRST(I) = {uid, if, while}
FIRST(A) = {uid}
FIRST(C) = {if}
FIRST(O) = {else, ε}            FOLLOW(O) = {fi} FIRST(W) = {while}
if, while}
if, while}
if, while, ε}                   FOLLOW(X) = {$, done, else, fi} if, while}
FIRST(E) = {c, uid}
FIRST(Y) = {<, =, !=, ε}        FOLLOW(Y) = {;, then, do} FIRST(E2) = {c, uid}
FIRST(Z) = {+, -, ε}            FOLLOW(Z) = {<, =, !=, ;, then, do} FIRST(T) = {c, uid}
FIRST(Op1) = {<, =, !=}
FIRST(Op2) = {+, -}
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
