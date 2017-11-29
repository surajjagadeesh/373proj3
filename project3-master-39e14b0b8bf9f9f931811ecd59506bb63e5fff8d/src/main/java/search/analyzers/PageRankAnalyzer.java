package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
    	ISet<URI> uris = new ChainedHashSet<URI>();
    	
    	IDictionary<URI, ISet<URI>> links = new ChainedHashDictionary<URI, ISet<URI>>();
    	for (Webpage page : webpages) {
    		uris.add(page.getUri());
        }
    	
    	for (Webpage page : webpages) {
    		IList<URI> pageLinks = page.getLinks();
    		ISet<URI> uri = new ChainedHashSet<URI>();
    		for (URI link : pageLinks) {
    			if (uris.contains(link) && !uri.contains(link) && !link.equals(page.getUri())) {
    				uri.add(link);
    			}
    		}
    		links.put(page.getUri(), uri);
    	}
    	return links;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
    	IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<URI, Double>();
    	int totalLinks = graph.size();
    	
    	for (KVPair<URI, ISet<URI>> webpage : graph) {
    		oldRanks.put(webpage.getKey(), 1. / totalLinks); //graph.size() might be wrong?	
    	}

        for (int i = 0; i < limit; i++) {
        	IDictionary<URI, Double> newRanks = new ChainedHashDictionary<URI, Double>();
        	
        	double initialValue = (1. - decay) / totalLinks;
        	for (KVPair<URI, ISet<URI>> webpage : graph) {
        		newRanks.put(webpage.getKey(), initialValue);
        	}
        	
        	for (KVPair<URI, ISet<URI>> webpage : graph) {
        		URI originalLink = webpage.getKey();
        		ISet<URI> links = webpage.getValue();
        		int length = links.size();
        		if (length == 0) {
        			double value = decay * oldRanks.get(originalLink) / totalLinks;
        			for (KVPair<URI, Double> ranks : newRanks) {
        				newRanks.put(ranks.getKey(), ranks.getValue() + value);
        			}
        		} else {
        			for (URI link : links) {
            			double value = decay * oldRanks.get(originalLink) / length;
            			newRanks.put(link, newRanks.get(link) + value);
            		}
        		}
        	}
        	
        	boolean finish = true;
        	for (KVPair<URI, Double> ranks : newRanks) {
        		if (Math.abs(ranks.getValue() - oldRanks.get(ranks.getKey())) >= epsilon) {
        			finish = false;
        			break;
        		}
			}
        	if (finish) {
        		return newRanks;
        	} else {
        		oldRanks = newRanks;
        	}
        }
        return oldRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
    	return pageRanks.get(pageUri);
        
    }
}
