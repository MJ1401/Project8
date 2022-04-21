package learning.markov;

import learning.core.Histogram;

import java.awt.*;
import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        // TODO: YOUR CODE HERE
        if (!label2symbol2symbol.containsKey(label)) {
            label2symbol2symbol.put(label, new HashMap<>());
        }
        if (!label2symbol2symbol.get(label).containsKey(prev)) {
            label2symbol2symbol.get(label).put(prev, new Histogram<>());
        }
        label2symbol2symbol.get(label).get(prev).bump(next);
    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        // TODO: YOUR CODE HERE
        ArrayList<Double> hold = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            Optional<S> prev;
            if (i > 0) {
                prev = Optional.of(sequence.get(i-1));
            } else {
                prev = Optional.empty();
            }
            if (!label2symbol2symbol.get(label).containsKey(prev)) {
                label2symbol2symbol.get(label).put(prev, new Histogram<>());
            }
            double w_prev = label2symbol2symbol.get(label).get(prev).getTotalCounts();
            double w_count = label2symbol2symbol.get(label).get(prev).getCountFor(sequence.get(i));
            hold.add((w_count+1)/(w_prev+1));
        }
        double r = 1;
        for (double h : hold) {
            r *= h;
        }
        return r;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    //
    // P(label | sequence) = P(sequence | label) * (P(label)/P(sequence))
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        LinkedHashMap<L, Double> r = new LinkedHashMap<>();
        for (L label : allLabels()) {
            double p_seq_lab = probability(sequence, label);
            double t = 0;
            for (L lang : allLabels()) {
                t += probability(sequence, lang);
            }
            r.put(label, p_seq_lab * (1/t));
        }
        return r;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        double b_prob = 0;
        LinkedHashMap<L, Double> results = labelDistribution(sequence);
        L best = null;
        for (L l : results.keySet()) {
            if (!results.containsKey(best)) {
                best = l;
            }
            if (results.get(l) > b_prob) {
                best = l;
                b_prob = results.get(l);
            }
        }
        return best;
    }
}
