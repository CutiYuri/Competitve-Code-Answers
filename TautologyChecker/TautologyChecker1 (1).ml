exception UnboundVariable of string

type formula = And  of formula * formula
	     | Implies of formula * formula
             | Or of formula * formula
	     | Not of formula 
	     | Prop of string
	     | True
             | False	

type subst = (string * bool) list


(* Some tautologies *)

(* P or (not P) *)
let t1 = Or (Prop "P", Not (Prop "P"))

(* P -> P *)
let t2 = Implies (Prop "P", Prop "P")

(* P -> Q or Q -> P *)
let t3 = Or (Implies (Prop "P", Prop "Q"), Implies (Prop "Q", Prop "P"))

(* (not P or Q) or (not Q or P) 
   - logically the same as t3 *)
let t4 = Or (Or ( Not (Prop "P"), Prop "Q"), Or ( Not (Prop "Q"), Prop "P"))



(* Some formulas that are not tautologies *)

(* P or Q *)
let nt1 = Or (Prop "P", Prop "Q")

(* P and Q *)
let nt2 = And (Prop "P", Prop "Q")

(* (P && Q) || (R -> Q) *)
let nt3 = Or (And ( Prop "P", Prop "Q"), Implies (Prop "R", Prop "Q"))


(* This functions might be called from inside `freevars`. *)
let rm_duplicates (lst: 'a list) : 'a list =
  let collect_unique to_keep elem =
    if List.mem elem to_keep then to_keep else elem::to_keep
  in List.rev (List.fold_left collect_unique [] lst)



(* Complete the `freevars` and `eval` functions below, and uncomment them.

   Note that `freevars` must not return duplicates, so you may want a
   helper function in `freevars` that does the recursive work of
   getting all instances of the free variables, as we did in the
   in-class examples, and then call `rm_duplicates` on that result to
   become the value returned by `freevars`.*)

let rec lookup (x: string) (s: subst) : bool =
  match s with
  |[] -> raise (UnboundVariable x)
  |(k,v)::rest -> if k = x then v else lookup x rest

let freevars (f: formula) : string list =
  let rec helper (f: formula) : string list =
    match f with
    |And(e1, e2)|Implies(e1, e2)|Or(e1, e2) -> helper e1 @ helper e2
    |Not(e) -> helper e
    |Prop(x) -> [x]
    |True -> []
    |False -> []
  in
  rm_duplicates (helper f)

let rec eval (f: formula)(s: subst): bool=
  match f with
  |And(e1, e2) -> (eval e1 s) && (eval e2 s)
  |Or(e1, e2) -> (eval e1 s) || (eval e2 s)
  |Implies(e1, e2) -> if eval e1 s = true then eval e2 s else true
  |Not(e) -> not (eval e s)
  |Prop(e) -> lookup e s
  |True -> true
  |False -> false   

