typealias Stack<T> = MutableList<T>

fun <T> Stack<T>.push(item: T) = add(item)
fun <T> Stack<T>.pop(): T = removeAt(lastIndex)
fun <T> Stack<T>.peek(): T = this[lastIndex]


enum class CurrentStage
{
    INITIALISATION,
    REVERSE_GRAPH_DFS,
    PRIORITIZED_GRAPH_DFS,
    COMPLETED
}


class AlgHandler<T>(private var graph : GraphOriented<T> = GraphOriented())
{
    private var currentStage : CurrentStage = CurrentStage.INITIALISATION
    private var searchStack : Stack<T> = mutableListOf<T>()
    private var handeledNodes : HashSet<T> = HashSet()
    private var nodesOrder : List<T> = emptyList<T>()
    private var connectComponents : MutableList<MutableList<T>> = mutableListOf<MutableList<T>>()
    var logs : String = String()
    // String and not observer, because it's necessary to save and restore logs

    private var history : Stack<AlgState<T>> = mutableListOf<AlgState<T>>()

    // Class for memento pattern, can be used in this class only
    private class AlgState<T>(val currentStage : CurrentStage = CurrentStage.INITIALISATION,
                              val graph : GraphOriented<T> = GraphOriented(),
                              val searchStack : Stack<T> = mutableListOf<T>(),
                              val handeledNodes : HashSet<T> = HashSet(),
                              val nodesOrder : List<T> = emptyList<T>(),
                              var connectComponents : MutableList<MutableList<T>> = mutableListOf<MutableList<T>>(),
                              var logs : String = String())

    fun refresh()
    {
        if (currentStage == CurrentStage.REVERSE_GRAPH_DFS)
            graph = graph.inversed()

        currentStage = CurrentStage.INITIALISATION
        searchStack  = mutableListOf<T>()
        handeledNodes = HashSet()
        nodesOrder = emptyList<T>()
        connectComponents = mutableListOf<MutableList<T>>()
        logs = String()

        history = mutableListOf<AlgState<T>>()
   }

    fun addNode(vertex: T)
    {
        graph.addEdge(vertex, vertex)
    }

    fun addEdge(sourceVertex: T, destinationVertex: T)
    {
        graph.addEdge(sourceVertex, destinationVertex)
    }

    // remove * can be done at initialisation state only -- otherwise it can damage agorithm 

    fun removeNode(vertex : T)
    {
        if (currentStage == CurrentStage.INITIALISATION)
        {
            graph.removeNode(vertex)
        }
    }

    fun removeEdge(sourceVertex: T, destinationVertex: T)
    {
        if (currentStage == CurrentStage.INITIALISATION)
        {
            graph.removeEdge(sourceVertex, destinationVertex)
        }
    }

    // Front end getters

    fun getEdges() : List<Pair<T,T>>
    {
        var list = graph.toList()
        return list.filter{it.first != it.second}   
    }

    fun getStackHead() : T?
    {
        if (searchStack.isNotEmpty())
            return searchStack.peek()
        else
            return null
    }

    fun getHandeledNodes() : List<T>
    {
        return this.handeledNodes.toList()
    }

    fun getNodes() : List<T>
    {
        return this.graph.nodes.toList()
    }

    fun getCurrentStage() : CurrentStage
    {
        return currentStage
    }

    fun getConnectComponents() : MutableList<MutableList<T>> 
    {
        return connectComponentsCopy()
    }

    override fun toString() : String
    {
        var res = String()
        val currentStageStr = currentStage.toString()
        val graphStr = graph.toString()
        val searchStackStr = searchStack.toString()
        val handeledNodesStr = handeledNodes.toString()
        val nodesOrderStr = nodesOrder.toString()
        val connectComponentsStr = this.connectComponentsToString()

        res += "currentStage is $currentStageStr\n"
        res += "graph is \n$graphStr\n"
        res += "searchStack is $searchStackStr\n"
        res += "handeledNodes is $handeledNodesStr\n"
        res += "nodesOrder is $nodesOrderStr\n"
        res += "connectComponents is $connectComponentsStr\n"
        
        return res
    }

    private fun save()
    {
        // The copy methodes created because Kotlin uses links by default
        history.push(AlgState<T>( currentStage = this.currentStage,
                                  graph = this.graph.copy(),
                                  searchStack = this.searchStack.toMutableList(),
                                  handeledNodes = HashSet(this.handeledNodes),
                                  nodesOrder = this.nodesOrder.toList(),
                                  connectComponents = this.connectComponentsCopy(),
                                  logs = this.logs ))
    }
    
    private fun restoreAlgState(algState : AlgState<T>)
    {
        this.currentStage = algState.currentStage
        this.graph = algState.graph
        this.searchStack = algState.searchStack
        this.handeledNodes = algState.handeledNodes
        this.nodesOrder = algState.nodesOrder
        this.connectComponents = algState.connectComponents
        this.logs = algState.logs
    }

    fun restorePrevState()
    {
        if (history.isNotEmpty())
        {
            val save = history.pop()
            restoreAlgState(save)
        }
    }

    fun connectComponentsToString() : String
    {
        var result = String()
        for (i in connectComponents)
        {
            var head = i.joinToString(separator = ", ", prefix = "[", postfix = "]")
            result += head + " "
        }
        return result
    }

    fun connectComponentsCopy() : MutableList<MutableList<T>>
    {
        var result = mutableListOf<MutableList<T>>()
        for (i in connectComponents)
        {
            result += i.toMutableList()
        }
        return result
    }

    fun addNodesNeighboursInStack(currentNode : T)
    {
        logs += ("The node isn't handeled yet\n")
        handeledNodes.add(currentNode)
        if (graph.adjacencyMap[currentNode] == null)
        {
            logs += ("No nodes to add to the stack\n")
            return
        }

        for (node in graph.adjacencyMap[currentNode]!!)
        {
            if (node !in handeledNodes)
            {
                logs += ("Adding node $node to the stack\n")
                searchStack.push(node)
            }
        }
    }

    fun doAlgStep()
    {
        println(toString())
        logs += "\n"    
        this.save()        
        when (currentStage)
        {
            CurrentStage.INITIALISATION -> 
            {
                logs += ("Initialisation of the algorithm\n")
                if (graph.nodes.size == 0)
                {
                    currentStage = CurrentStage.COMPLETED
                    return
                }
                
                refresh()

                currentStage = CurrentStage.REVERSE_GRAPH_DFS
                searchStack.add(graph.nodes.first())
                logs += ("Go to the next stage\nReverse graph DFS\n")
                logs += ("Inversing graph...\n")
                graph = graph.inversed()    
                connectComponents.add(mutableListOf<T>())
                return
            }

            CurrentStage.REVERSE_GRAPH_DFS -> 
            {
                if (searchStack.isNotEmpty())
                {
                    val currentNode = searchStack.peek()
                    logs += ("Handeling node $currentNode\n")

                    if (currentNode !in handeledNodes)
                    {
                        addNodesNeighboursInStack(currentNode)
                    }

                    else
                    {
                        logs += ("This node has already been handeled\n")
                        logs += ("Deleting it from the stack\nand adding it to the nodes order\n(if there is no this node yet)\n")
                        searchStack.pop()
                        if (currentNode !in nodesOrder)
                        {
                            nodesOrder += currentNode
                            logs += ("Now nodes order is $nodesOrder\n")
                        }
                    }
                }

                else // Local stop, but there is nodes left
                {
                    logs += ("There are no nodes left in the stack\n")
                    if (handeledNodes.size == graph.nodes.size) // Aka there is no not handeled nodes
                    {
                        logs += ("All nodes have been handeled\n")
                        logs += ("Preparing for the next stage\nPrioritized graph DFS\n")
                        logs += ("Inversing graph back...\n")
                        currentStage = CurrentStage.PRIORITIZED_GRAPH_DFS
                        graph = graph.inversed()
                        handeledNodes.clear()
                        nodesOrder = nodesOrder.reversed()
                        searchStack.add(nodesOrder[0])
                        return
                    }
                    
                    for (i in graph.nodes)
                    {
                        if (i !in handeledNodes)
                        {
                            logs += ("Adding node $i to the stack\n")
                            searchStack.push(i)
                            return
                        }
                    }
                    throw Exception("No unhandelend nodes found but nodes.size != handeledNodes.size")
                }
                return
            }

            CurrentStage.PRIORITIZED_GRAPH_DFS ->
            {
                if (searchStack.isNotEmpty())
                {
                    val currentNode = searchStack.peek()
                    logs += ("Handeling node $currentNode\n")
                    
                    if (currentNode !in handeledNodes)
                    {
                        addNodesNeighboursInStack(currentNode)
                    }

                    else
                    {
                        logs += ("This node has already been handeled\n")
                        logs += ("Adding node to current connect components\n")
                        searchStack.pop()
                        var tail = connectComponents.last()
                        connectComponents.removeAt(connectComponents.size -1)
                        if (currentNode !in tail)
                            tail.add(currentNode)
                        connectComponents.add(tail)
                        logs += ("Now connect components are ${connectComponentsToString()}\n")
                    }
                }

                else
                {
                    logs += ("There are no nodes left in the stack\n")
                    if (handeledNodes.size == graph.nodes.size) // Aka there is no not handeled nodes
                    {
                        logs += ("All nodes have been handeled\n")
                        logs += ("Now the algotihm has stopped working\n")
                        currentStage = CurrentStage.COMPLETED
                        handeledNodes.clear()
                        return
                    }

                    for (i in nodesOrder)
                    {
                        if (i !in handeledNodes)
                        {
                            searchStack.push(i)
                            logs += ("Adding node $i,\n choosed by nodes order $nodesOrder,\n to the stack\n")
                            logs += ("And finishing this connection component\n")
                            logs += ("Now the components are ${connectComponentsToString()}\n")
                            connectComponents.add(mutableListOf<T>())
                            return
                        }
                    }
                    throw Exception("No unhandelend nodes found but nodes.size != handeledNodes.size")
                }
                return                    
            }

            CurrentStage.COMPLETED ->
            {
                logs += ("The algorithm is complete and the components are ${connectComponentsToString()}")
                return
            } 
        }
    }

    fun doAlgUntilCompleted()
    {   
        while (currentStage != CurrentStage.COMPLETED)
        {
            doAlgStep()
        }
    }
}

