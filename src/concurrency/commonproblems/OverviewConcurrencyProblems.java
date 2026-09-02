package concurrency.commonproblems;

/*
                     Concurrency Common Problems
                              │
              ┌───────────────┴────────────────────┐
              │                                    │
       Failure Problems                      Safety Techniques
              │                                    │
    ┌─────────┼─────────┬─────────┐         ┌──────┼────────────┐
    │         │         │         │         │      │            │
   Race    Deadlock  Livelock  Starvation Thread Thread  Immutability
 Condition                                Safety Confinement

*/
class OverviewConcurrencyProblems {
    public static void main(String[] args) {
        System.out.println("Overview of common problems and safety techniques in concurrency");
    }
}
