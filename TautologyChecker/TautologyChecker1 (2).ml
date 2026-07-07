(* Copy the contents of your `warmup.ml` file here, at the top
   of `literacy.ml`.
 *)

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

let show_list (show: 'a -> string) (lst: 'a list) : string =
  let rec sl lst =
    match lst with 
    | [] -> ""
    | [x] -> show x
    | x::xs -> show x ^ "; " ^ sl xs
  in "[ " ^ sl lst ^ " ]"

let show_string_bool_pair (s,b) =
  "(\"" ^ s ^ "\"," ^ (if b then "true" else "false") ^ ")"

let show_subst (s: subst) : string = show_list show_string_bool_pair s

let string_of_chars (chars : char list) : string =
  String.of_seq (List.to_seq chars)

let chars_of_string (str : string) : char list =
  List.of_seq (String.to_seq str)

let rm_duplicates (lst: 'a list) : 'a list =
  let collect_unique to_keep elem =
    if List.mem elem to_keep then to_keep else elem::to_keep
  in List.rev (List.fold_left collect_unique [] lst)


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



let rec try_option (env_so_far: subst) (free_vars: string list) (f: formula) : 
          subst option =
  match free_vars with
  | [] -> let result = eval f env_so_far in 
                      if result = false then Some env_so_far else None
  |x::xs -> match try_option ((x, true)::env_so_far) xs f with
            |Some s -> Some s
            |None -> try_option ((x, false)::env_so_far) xs f




(* You should not change this function. *)
let tautology_checker_option (f:formula) : subst option =
  try_option [] (freevars f) f


