Sorbonne University, Master Informatics, DAAR (Développement des Algorithmes d’Application Réticulaire)

# (unweighted undirected) Unit Disk Graph with Algos
*UDG*, *Unit Disk Graph*, *Geometric graph* G=(V,E) in a 2D plane: a set of points called vertices, and a threshold on the distance between the points : there is an edge between two vertices if and only if the Euclidean distance between the two vertices is smaller than this threshold.
  
## FVS, Feedback Vertex Set
*Feedback Vertex Set* is a minimum sized subset of vertices F ⊆ V such that the subgraph G[V\F] induced by V\F in G is cycleless.
  - Greedy algo
  - Algo of the article "A 2-approximation algorithm for the undirected feedback vertex set problem" by Bafna, Berman, Fujito
  - Optimisation by Local Search 

## MIS, Maximal Independent Set
*Maximal Independent Set* is a subset of vertices I⊆V such that the subgraph G[I] induced by I is edgeless, and such that the subset I is maximal by inclusion.
  - Naive algo
  - Optimisation by Local Search 
  - Non-distribied version of the algo MIS of the article "Connected Domination in Multihop Ad Hoc Wireless Networks" by Cardei, Cheng, Cheng, Du 

## DS, Dominating Set
*Minimum Dominating Set* is a minimum sized subset of vertices D⊆V such that every vertex v∈V either belong to D or is a neighbour
of a vertex in D.
  - Greedy algo for a connected graph
  - Optimisation by Local Search 
  - Optimisation of the article "A PTAS for the minimum domimation set problem in unit disk graphs" by Nieberg, Hurink - soon

## CDS, Conected Domitating Set
*Ninimum Connected Dominating Set* is a minimum sized subset of vertices D⊆V such that every vertex v∈V either belong to D or is a neighbour of a vertex in D, and such that the subgraph G[D] induced by D is connected.
  - Algo of the article "On greedy construction of CDS in wireless networks" by Yingshu, Thai, Wang, Yi, Wan, Du
  - Optimisation by Local Search 
