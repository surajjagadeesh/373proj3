//Suraj Jagdaeesh and Allen Putich

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
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    private IDictionary<URI, Double> normValues;

    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normValues = this.norm(documentTfIdfVectors);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * This method should return a dictionary mapping every single unique word found
     * in any documents to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
    	IDictionary<String, Double> wordCount = new ChainedHashDictionary<String, Double>();
    	for (Webpage page : pages) {
        	IList<String> words = page.getWords();
        	ISet<String> x = new ChainedHashSet<>();
        	for (String word : words) {
        		if (!x.contains(word)) {
        			if (!wordCount.containsKey(word)) {
            			wordCount.put(word, 1.);
            		} else {
            			wordCount.put(word, wordCount.get(word) + 1.);
            		}
        			x.add(word);
        		}
        	}	
        }
    	int numPages = pages.size();
    	for (KVPair<String, Double> pair : wordCount) {
            String key = pair.getKey();
            wordCount.put(key, Math.log(numPages / pair.getValue()));
        }
    	return wordCount;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
    	IDictionary<String, Double> wordCount = new ChainedHashDictionary<String, Double>();
    	if (words.size() == 0) {
    		return wordCount;
    	}
    	double totalWords = 1.0 / words.size();
    	for (String word : words) {
    		if (!wordCount.containsKey(word)) {
    			wordCount.put(word, totalWords);
    		} else {
    			wordCount.put(word, wordCount.get(word) + totalWords);
    		}
        }
    	return wordCount;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
    	// Hint: this method should use the idfScores field and
    	// call the computeTfScores(...) method.
    	IDictionary<URI, IDictionary<String, Double>> tfIDF = new ChainedHashDictionary<URI, 
    			IDictionary<String, Double>>();
    	IDictionary<String, Double> tfScores;
    	for (Webpage page : pages) {
    		URI pageURI = page.getUri();
    		IList<String> words = page.getWords();
    		tfScores = computeTfScores(words);
			for (KVPair<String, Double> pair : tfScores) {
	            String key = pair.getKey();
	            tfScores.put(key, pair.getValue() * idfScores.get(key));
	        }        	
        	tfIDF.put(pageURI, tfScores);
        }
    	return tfIDF;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
    	IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
    	
    	IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
    	IDictionary<String, Double> tfScoreQuery = computeTfScores(query);
    	for (String word : query) {
    		if (!idfScores.containsKey(word)) {
    			queryVector.put(word, 0.);
    		} else {
    			queryVector.put(word, tfScoreQuery.get(word) * idfScores.get(word));
    		}
    	}
    	
    	double numerator = 0.0;    	
    	double normQueryVector = 0.0;
    	for (KVPair<String, Double> pair : queryVector) {
            String word = pair.getKey();
            Double value = pair.getValue();
            double docWordScore = 0.0;
    		if (documentVector.containsKey(word)) {
    			docWordScore = documentVector.get(word);
    		}
    		numerator += docWordScore * value;
    		normQueryVector += (value * value);
        }
    	normQueryVector = Math.sqrt(normQueryVector);
    	
    	
    	Double denominator = normValues.get(pageUri) * normQueryVector;
    	if (denominator != 0) {
    		return numerator / denominator;
    	}
    	return 0.;
    	
    	
    	// this smethod, you should:
    	//
    	// 1. Figure out what information can be precomputed in your constructor.
    	//    Add a third field containing that information.
    	//
    	// 2. See if you can combine or merge one or more loops.
    }
    
    private IDictionary<URI, Double> norm(IDictionary<URI, IDictionary<String, Double>> vector) {
    	IDictionary<URI, Double> normValue = new ChainedHashDictionary<URI, Double>();
		for (KVPair<URI, IDictionary<String, Double>> pair : vector) {
			Double output = 0.0;
			IDictionary<String, Double> score = pair.getValue();
			for (KVPair<String, Double> pair2 : score) {
				Double score2 = pair2.getValue();
				output += score2 * score2;
			}
            normValue.put(pair.getKey(), Math.sqrt(output));
        }
		return normValue;
    }   
}
