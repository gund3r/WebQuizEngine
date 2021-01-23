package engine.entities;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class Answer {

    @Size(max = 4)
    List<String> answer = new ArrayList<>();

    public Answer() {}

    public Answer(@Size(max = 4) List<String> answer) {
        this.answer = answer;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
