# TemporalGraphs
Portal is a system for the exploration of evolving graph data structures.Portal is a system for efficient querying and exploratory analysis of evolving graphs, built on Apache Spark. At a high level, Portal extends Apache Spark’s graph library, GraphX, by introducing a new Temporal Graph abstraction (TGraph) with four concrete implementations: RepresentativeGraph, OneGraph, HybridGraph and Vertex Edge. To support evolving graph computation, Portal exposes a set of fundamental operators and analytics for TGraph.

## Why?
Many systems provide query abstractions over graph data structures. These include both graph databases, such as neo4j, and cluster computing frameworks, such as Apache Spark. However, no open source framework for the query of evolving graphs has become widely used.

Graph data structures are ubiquitous in the world (The Web, Social Networks, and Transportation Networks are just a few broad categories of application). Thus, it stands to reason that we can ask many interesting questions of graphs as they change over time.

### Example using IMDB Dataset


<a href="https://github.com/codingisbliss/TemporalGraphs/blob/master/data/IMDB%20Report.pdf" target="_blank">A Complete Report for IMDB dataset using Portal
</a>

### Installations 

1. Download the <a href = "https://www.docker.com/products/container-runtime#/download" target="_blabk">Docker Client.</a>
2. Pull  Portal's docker image:
```python
docker pull portaldb/zeppelin:latest
```
3. Run the container
```shell
  docker run -p 8080:8080 portaldb/zeppelin:latest
```
4. Download the IMDB dataset (7 GB) from <a href = "https://www.docker.com/products/container-runtime#/download" target="_blabk">here.</a>
5. copy the dowloaded files in /data of zepllien.
6. Run the imdb_portal_graphs.scala in the notebook.
