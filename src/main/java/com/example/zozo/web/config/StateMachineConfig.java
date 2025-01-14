package com.example.zozo.web.config;

import com.example.zozo.web.model.StatesAndEvents;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine(name = "tradeStateMachine")
public class StateMachineConfig extends StateMachineConfigurerAdapter<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> states) throws Exception {
        states
                .withStates()
                .initial(StatesAndEvents.GetStockPriceStates.INITIATED)
                .state(StatesAndEvents.GetStockPriceStates.PROCESSING)
                .end(StatesAndEvents.GetStockPriceStates.COMPLETED)
                .state(StatesAndEvents.GetStockPriceStates.FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> transitions) throws Exception {
        transitions
                .withExternal().source(StatesAndEvents.GetStockPriceStates.INITIATED).target(StatesAndEvents.GetStockPriceStates.PROCESSING).event(StatesAndEvents.GetStockPriceEvents.START_PROCESS)
                .and()
                .withExternal().source(StatesAndEvents.GetStockPriceStates.PROCESSING).target(StatesAndEvents.GetStockPriceStates.COMPLETED).event(StatesAndEvents.GetStockPriceEvents.SUCCESS)
                .and()
                .withExternal().source(StatesAndEvents.GetStockPriceStates.PROCESSING).target(StatesAndEvents.GetStockPriceStates.FAILED).event(StatesAndEvents.GetStockPriceEvents.FAILURE);
    }
}
