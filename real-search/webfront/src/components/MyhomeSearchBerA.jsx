import React, { Component } from "react";
import { Table } from 'antd';

const columns = [
    {
        title: 'Price',
        dataIndex: 'PriceAsString',
        key: 'PriceAsString',
    },
    {
        title: 'Ber Rating',
        dataIndex: 'BerRating',
        key: 'BerRating',
    },
    {
        title: 'Address',
        dataIndex: 'DisplayAddress',
        key: 'DisplayAddress',
    },
    {
        title: 'Living Area',
        dataIndex: 'SizeStringMeters',
        key: 'SizeStringMeters',
    },
    {
        title: 'Detail Link',
        dataIndex: 'BrochureUrl',
        key: 'BrochureUrl',
        render: url => {
            return <a href={"https://www.myhome.ie" + url}>MyHome</a>
        }
    },
    {
        title: 'Image',
        dataIndex: 'MainPhotoWeb',
        key: 'MainPhotoWeb',
        render: mediumImageLink => <img alt={mediumImageLink} src={mediumImageLink} />
    },
];


class MyhomeSearchBerA extends Component {
    state = {
        resultCount: 0,
        SearchResults: [],
    };

    searchMyhomeBerA = async (event) => {
        event.preventDefault();
        let response = await fetch('/myhomebera');
        let body = await response.json();
        this.setState({ resultCount: body.ResultCount, SearchResults: body.SearchResults });
    }

    render() {
        return (
            <div class="tab">
                <button onClick={(event) => this.searchMyhomeBerA(event)}>Search</button>
                <h2>Properties Found {this.state.SearchResults.length}</h2>
                <Table dataSource={this.state.SearchResults} columns={columns} />
            </div>
        );
    }
}
export default MyhomeSearchBerA