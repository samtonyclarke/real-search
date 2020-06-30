import React, { Component } from "react";
import { Table } from 'antd';

const columns = [
    {
        title: 'Price',
        dataIndex: 'price',
        key: 'price',
    },
    {
        title: 'Year Built',
        dataIndex: ['hdpData','homeInfo','yearBuilt'],
        key: ['hdpData','homeInfo','yearBuilt'],
    },
    {
        title: 'Address',
        dataIndex: 'address',
        key: 'address',
    },
    {
        title: 'Living Area',
        dataIndex: ['hdpData','homeInfo','livingArea'],
        key: ['hdpData','homeInfo','livingArea'],
    },
    {
        title: 'Detail Link',
        dataIndex: 'detailUrl',
        key: 'detailUrl',
        render: url => <a href={url}>Zillow</a>
    },
    {
        title: 'Image',
        dataIndex: ['hdpData','homeInfo','mediumImageLink'],
        key: ['hdpData','homeInfo','mediumImageLink'],
        render: mediumImageLink => <img alt={mediumImageLink} src={mediumImageLink} />
    },
];


class Zillow extends Component {
    state = {
        resultCount: 0,
        listResults: []
    };

    searchZillow = async (event) => {
        event.preventDefault();
        let response = await fetch('/zillow');
        let body = await response.json();
        this.setState({ resultCount: body.searchList.totalResultCount, listResults: body.searchResults.listResults });
    }

    render() {
        return (
            <div class="tab">
                <button onClick={(event) => this.searchZillow(event)}>Search</button>
                <h2>Properties Found {this.state.resultCount}</h2>
                <Table dataSource={this.state.listResults} columns={columns} />
            </div>
        );
    }
}
export default Zillow