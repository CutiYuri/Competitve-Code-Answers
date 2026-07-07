type 'a tree1 = Empty
                |Leaf of 'a tree1 * 'a * 'a tree1



let rec reduce(base: 'b)(f: 'b -> 'a -> 'b -> 'b)(tree: 'a tree1): 'b=
  match tree with
  |Empty -> base
  |Leaf(l, v, r) -> f (reduce base f l) v (reduce base f r)

let sum(tree: int tree1): int =
  reduce 0 (fun l v r -> l + v + r) tree



