package engine.entities;

public class Feedback {

    private boolean success;
    private String feedback;

    public Feedback(boolean success) {
        this.success = success;
        if (!this.success) {
            this.feedback = "Wrong answer! Please, try again.";
        } else {
            this.feedback = "Congratulations, you're right!";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Answer{"
                + "success=" + success
                + ", feedback='" + feedback + '\''
                + '}';
    }
}
