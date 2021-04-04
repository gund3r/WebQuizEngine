package engine;

import engine.controllers.QuizController;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.*;

public class WebQuizEngineTest {

    Pageable pageable = new Pageable() {
        @Override
        public int getPageNumber() {
            return 0;
        }

        @Override
        public int getPageSize() {
            return 0;
        }

        @Override
        public long getOffset() {
            return 0;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    };

    @Test public void testAppHasAGreeting() {
        QuizController classUnderTest = new QuizController();
        assertNotNull("app should have a greeting", classUnderTest.getAllQuizzes(pageable));
    }

}

