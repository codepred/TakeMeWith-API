package codepred.counter;

import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    public CounterService(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    public void increaseCounter(){
        Counter counter = new Counter();
        if(counterRepository.findAll().size() == 0){
            counter.setCounter(1);
            counterRepository.save(counter);
        }
        else {
            counter = counterRepository.findAll().get(0);
            counter.setCounter(counter.getCounter() + 1);
            counterRepository.save(counter);
        }
    }

}
