package ie.dcu.cngl.summarizer.feature;

public class FeatureScore {

	private String featureName;
	private Double[] scores;
	
	
	public FeatureScore(String featureName, Double[] scores) {
		
		this.featureName = featureName;
		this.scores = scores;
	}


	public String getFeatureName() {
		return featureName;
	}


	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}


	public Double[] getScores() {
		return scores;
	}


	public void setScores(Double[] scores) {
		this.scores = scores;
	}
	
	
	
	

}
