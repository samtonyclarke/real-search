import React, { Component } from "react";

class MyhomePriceChange extends Component {
    state = {
        priceChangeHistory: 0.0,
        priceChangeCount: 0,
    };


    priceChangeMyhome = async (event) => {
        event.preventDefault();
        let response = await fetch('/myhomepricechange');
        let body = await response.json();
        let change = 0.0
        Object.keys(body.Results).forEach((e) => {
            change = change + body.Results[e].PriceChangePercentage
          });
        change = change / body.Results.length;
        this.setState({ priceChangeHistory: change });
        this.setState( {priceChangeCount: body.Results.length});

    }


    render() {
        return (
            <div class="tab">
                <button onClick={(event) => this.priceChangeMyhome(event)}>Price Change</button>
                <h2>Price Change from last {this.state.priceChangeCount} properites: {this.state.priceChangeHistory} %</h2>
            </div>
        );
    }
}
export default MyhomePriceChange