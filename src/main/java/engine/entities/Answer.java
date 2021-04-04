package engine.entities;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public final class Answer {

    @Size(max = 4)
    private List<String> answer = new ArrayList<>();

    public Answer() { }

    public Answer(List<String> answer) {
        this.answer = answer;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
