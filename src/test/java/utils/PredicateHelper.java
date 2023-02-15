package utils;

import org.hamcrest.Condition;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public interface PredicateHelper  {
    static PredicateHelper ifTrue(boolean condition) {
        return condition ? new TrueCondition() : new FalseCondition();
    }

    PredicateHelper and(boolean condition);

     void then(Runnable func);


    class TrueCondition implements PredicateHelper {

        @Override
        public PredicateHelper and(boolean condition) {
            return condition ? new TrueCondition() : new FalseCondition();
        }

        @Override
        public void then(Runnable func) {
            func.run();
        }


    }

    class FalseCondition implements PredicateHelper {
        @Override
        public PredicateHelper and(boolean condition) {
            return this;
        }

        @Override
        public void then(Runnable func) {
            // Do nothing
        }
    }
}
