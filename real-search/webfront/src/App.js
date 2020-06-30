import React, { Component } from 'react';
import { Tabs } from 'antd';
import Zillow from "./components/zillow";
import MyhomeFavourites from "./components/MyhomeFavourites";
import MyhomeSearchRecovery from './components/MyhomeSearchRecovery';
import MyhomeSearchBerA from './components/MyhomeSearchBerA';
import MyhomePriceChange from "./components/MyhomePriceChange";
import './App.css';
import 'antd/dist/antd.css';

const { TabPane } = Tabs;

class App extends Component {

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <Tabs defaultActiveKey="1">
            <TabPane tab="US" key="1">
              <Zillow />
            </TabPane>
            <TabPane tab="Search Ireland with Heat Recovery" key="2">
              <MyhomeSearchRecovery />
            </TabPane>
            <TabPane tab="Search Ireland A3 or higher" key="3">
              <MyhomeSearchBerA />
            </TabPane>
            <TabPane tab="Favourites Ireland" key="4">
              <MyhomeFavourites />
            </TabPane>
            <TabPane tab="Price Change Ireland" key="5">
              <MyhomePriceChange />
            </TabPane>
          </Tabs>
        </header>
      </div>
    );
  }
}
export default App;