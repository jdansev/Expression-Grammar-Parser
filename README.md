# Grammar-Parser

```
Grammar G:
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
