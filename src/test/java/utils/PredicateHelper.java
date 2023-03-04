package utils;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public interface PredicateHelper {
    static PredicateHelper ifTrue(boolean condition) {
        return condition ? new TrueCondition() : new FalseCondition();
    }

    PredicateHelper and(boolean condition);

    PredicateHelper then(Runnable func);
    void elseDo(Runnable func);


    class TrueCondition implements PredicateHelper {

        @Override
        public PredicateHelper and(boolean condition) {
            return condition ? new TrueCondition() : new FalseCondition();
        }

        @Override
        public PredicateHelper then(Runnable func) {
            func.run();
            return this;
        }

        @Override
        public void elseDo(Runnable func) {
            // Do nothing
        }


    }

    class FalseCondition implements PredicateHelper {
        @Override
        public PredicateHelper and(boolean condition) {
            return this;
        }

        @Override
        public PredicateHelper then(Runnable func) {
            return this;
        }

        @Override
        public void elseDo(Runnable func) {
            func.run();
        }
    }
}
