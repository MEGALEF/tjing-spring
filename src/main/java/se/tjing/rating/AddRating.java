package se.tjing.rating;

public class AddRating {
	private Integer interactionId;
	private String text;
	private RatingGrade grade;
	
	public Integer getInteractionId() {
		return interactionId;
	}
	public void setInteractionId(Integer interactionId) {
		this.interactionId = interactionId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public RatingGrade getGrade() {
		return grade;
	}
	public void setGrade(RatingGrade grade) {
		this.grade = grade;
	}
	
	
}
