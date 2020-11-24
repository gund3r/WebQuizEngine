package main.java;

public class Feedback {

    boolean success;
    String feedback;

    public Feedback(boolean success) {
        this.success = success;
        if (false == this.success) {
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
        return "Answer{" +
                "success=" + success +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
