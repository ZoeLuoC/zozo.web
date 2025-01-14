package com.example.zozo.web.service;

import com.example.zozo.web.model.StatesAndEvents;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
public class StateMachineListener extends StateMachineListenerAdapter<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> {
    @Override
    public void stateChanged(State<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> from, State<StatesAndEvents.GetStockPriceStates, StatesAndEvents.GetStockPriceEvents> to) {
        System.out.println("State changed from" + (from != null ? from.getId() : "null") + " to " + to.getId());
    }
}
