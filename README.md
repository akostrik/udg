Sorbonne University, DAAR (Développement des Algorithmes d’Application Réticulaire), Master Informatics 2021/2022 

# UDG, Unit Disk Graph with Algos
*UDP*, *Geometric graph* G=(V,E) in a 2D plane: a set of points in the plane called vertices, and a threshold on the distance between the points : there is an edge between two vertices if and only if the Euclidean distance between the two vertices is smaller than this threshold.
  
## k-means
The k-means clustering problem consists of computing a partition of S into k parties in such a way that the total sum of distances between each element of a party to the barycenter of that party is minimized.
  - Algo de l'article "k-means++: The advantages of careful seeding" by Arthur, Vassilvitskii - soon

## FVS, Feedback Vertex Set
*Feedback Vertex Set* is a minimum sized subset of vertices F ⊆ V such that the subgraph G[V\F] induced by V\F in G is cycleless.
  - Greedy
  - Optimisation by Local Search 
  - Algo de l'article "A 2-approximation algorithm for the undirected feedback vertex set problem" by Bafna, Berman, Fujito (1999) - soon

## MIS, Maximal Independent Set
  - Algo naiv
  - Non-distribied version of the algo MIS (MIS with property of 2-hops distance) of the article "Connected Domination in Multihop Ad Hoc Wireless Networks" by Cardei, Cheng, Cheng, Du (2002)

## DS, Dominating Set
*Minimum Dominating Set* is a minimum sized subset of vertices D⊆V such that every vertex v∈V either belong to D or is a neighbour
of a vertex in D.
  - Greedy
  - Optimisation by Local Search 
  - Optimisation of the article "A PTAS for the minimum domimation set problem in unit disk graphs" by Nieberg, Hurink (2008) - soon

## CDS, Conected Domitating Set
*Ninimum Connected Dominating Set* is a minimum sized subset of vertices D⊆V such that every vertex v∈V either belong to D or is a neighbour of a vertex in D, and such that the subgraph G[D] induced by D is connected.
  - Algo of the article "On greedy construction of CDS in wireless networks" by Yingshu, Thai, Wang, Yi, Wan, Du (2005) (the program only, no report)
