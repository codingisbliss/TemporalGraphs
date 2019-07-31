# TemporalGraphs
Portal is a system for the exploration of evolving graph data structures.Portal is a system for efficient querying and exploratory analysis of evolving graphs, built on Apache Spark. At a high level, Portal extends Apache Spark’s graph library, GraphX, by introducing a new Temporal Graph abstraction (TGraph) with four concrete implementations: RepresentativeGraph, OneGraph, HybridGraph and Vertex Edge. To support evolving graph computation, Portal exposes a set of fundamental operators and analytics for TGraph.

## Why?
Many systems provide query abstractions over graph data structures. These include both graph databases, such as neo4j, and cluster computing frameworks, such as Apache Spark. However, no open source framework for the query of evolving graphs has become widely used.

Graph data structures are ubiquitous in the world (The Web, Social Networks, and Transportation Networks are just a few broad categories of application). Thus, it stands to reason that we can ask many interesting questions of graphs as they change over time.
Given a particular person’s popularity on a social network, we may want to see how that popularity is trending upwards or downwards over the past year or month.
Given a particular transportation network, we may want to see how remote destinations are becoming more or less accessible, compared year over year.
When we frame many time based analytics in terms of graph data structures, we see that a framework for evolving graphs analysis begs exploration.

### Example by IMDB Dataset

<embed src="https://sumanbogati.github.io/sample.pdf" type="application/pdf" />
