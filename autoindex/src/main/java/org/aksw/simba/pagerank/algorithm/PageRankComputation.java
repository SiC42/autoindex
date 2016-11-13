package org.aksw.simba.pagerank.algorithm;

import java.util.List;

import org.aksw.simba.pagerank.definitions.RankedNode;
import org.aksw.simba.pagerank.definitions.RankedTriple;
import org.aksw.simba.pagerank.input.ProcessedInput;
import org.jblas.DoubleMatrix;

public class PageRankComputation {
	ProcessedInput input = new ProcessedInput(
			"ekaw-2012-complete.ttl");
	DoubleMatrix triples2Nodes;
	DoubleMatrix nodes2Triples;
	DoubleMatrix pMatrixTriples;
	double dampingFactor;
	double pDistributionInitialVal;
	DoubleMatrix pDistributionMatrix;

	public static void main(String[] args) {
		PageRankComputation p = new PageRankComputation();
		p.computePR();
	}

	public PageRankComputation() {

		this.triples2Nodes = DoubleMatrix.zeros(input.getNumberofTriples() + 1,
				input.getNumberofResources() + 1);
		this.nodes2Triples = DoubleMatrix.zeros(
				input.getNumberofResources() + 1,
				input.getNumberofTriples() + 1);
		this.pMatrixTriples = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionMatrix = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		this.pDistributionInitialVal = input.getNumberofResources()
				/ (input.getNumberofTriples() * (input.getNumberofResources() + input
						.getNumberofTriples()));
		this.dampingFactor = 0.85;

	}

	public void computePR() {
		this.createTriples2NodesMatrix(input.getListOfTriples(),
				input.getListOfResources());
		this.createNode2TripleMatrix(input.getListOfTriples(),
				input.getListOfResources());
		this.computeProbabilityTripleMatrix();

		this.initializeProbabilityDistributionMatrix();
		this.triples2Nodes.print();
		DoubleMatrix initialDistributionMatrix = DoubleMatrix.zeros(
				input.getNumberofTriples() + 1, input.getNumberofTriples() + 1);
		DoubleMatrix identityMatrix = DoubleMatrix.eye(input
				.getNumberofTriples() + 1);
		DoubleMatrix pMatrixTriplesTranspose = pMatrixTriples.transpose();
		int counter = 0;

		while (counter == 10) {
			counter++;
			initialDistributionMatrix = pDistributionMatrix;
			pDistributionMatrix = (pMatrixTriplesTranspose.muli(dampingFactor))
					.mmul(initialDistributionMatrix.add(identityMatrix
							.muli(dampingFactor)));

		}

//		 pDistributionMatrix.print();

	}

	public void createTriples2NodesMatrix(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {

		for (RankedTriple r : listofTriples) {
			int index = listofNodes.indexOf(r.getSubject());
			if (index == -1) {
				index = listofNodes.indexOf(r.getPredicate());
			}
			if (index == -1) {
				index = listofNodes.indexOf(r.getObject());
			}
			if (index != -1) {
				System.out.println(listofTriples.indexOf(r) +"  "+ index+"  "+ 1.0 / 3.0);

				triples2Nodes.put(listofTriples.indexOf(r), index, 1.0 / 3.0);
			}
		}
	}

	public void calculateTriplesofNodes(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {
		int triplecount = 0;
		//System.out.println(listofNodes);

		for (RankedNode c : listofNodes)

		{
			triplecount = 0;
			for (RankedTriple r : listofTriples) {
				if (c.equals(r.getSubject()) || c.equals(r.getPredicate())
						|| c.equals(r.getObject()))

				{
					triplecount++;
				}

			}
			c.setNumberOfTriples(triplecount);
		}
	}

	public void createNode2TripleMatrix(List<RankedTriple> listofTriples,
			List<RankedNode> listofNodes) {

		this.calculateTriplesofNodes(listofTriples, listofNodes);

		for (RankedNode c : listofNodes)

		{

			for (RankedTriple r : listofTriples) {
				if (c.equals(r.getSubject()) || c.equals(r.getPredicate())
						|| c.equals(r.getObject()))

				{
					nodes2Triples.put(listofTriples.indexOf(r),
							listofNodes.indexOf(c), 1 / c.getNumberOfTriples());
				}

			}
		}
	}

	public void computeProbabilityTripleMatrix() {
		pMatrixTriples = triples2Nodes.mmul(nodes2Triples);

	}

	public void initializeProbabilityDistributionMatrix() {
		int row = input.getNumberofTriples();
		for (int y = 0; y < row; y++) {
			for (int x = 0; x < row; x++) {
				pDistributionMatrix.put(y, x, pDistributionInitialVal);
			}
		}

	}
}