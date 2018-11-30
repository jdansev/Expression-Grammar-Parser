# First and follow sets
```
FIRST(S) = {uid, if, while}
FIRST(L) = {uid, if, while}
FIRST(X) = {uid, if, while, ε}  FOLLOW(X) = {$, done, else, fi}
FIRST(I) = {uid, if, while}
FIRST(A) = {uid}
FIRST(C) = {if}
FIRST(O) = {else, ε}            FOLLOW(O) = {fi}
FIRST(W) = {while}
FIRST(E) = {c, uid}
FIRST(Y) = {<, =, !=, ε}        FOLLOW(Y) = {;, then, do}
FIRST(E2) = {c, uid}
FIRST(Z) = {+, -, ε}            FOLLOW(Z) = {<, =, !=, ;, then, do}
FIRST(T) = {c, uid}
FIRST(Op1) = {<, =, !=}
FIRST(Op2) = {+, -}
```
