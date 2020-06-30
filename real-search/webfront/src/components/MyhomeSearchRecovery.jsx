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


class MyhomeSearchRecovery extends Component {
    state = {
        resultCount: 0,
        SearchResults: [],
    };

    searchMyhome = async (event) => {
        event.preventDefault();
        let response = await fetch('/myhomerecovery');
        let body = await response.json();
        this.setState({ resultCount: body.ResultCount, SearchResults: body.SearchResults });
    }

    render() {
        return (
            <div class="tab">
                <button onClick={(event) => this.searchMyhome(event)}>Search</button>
                <h2>Properties Found {this.state.resultCount}</h2>
                <Table dataSource={this.state.SearchResults} columns={columns} />
            </div>
        );
    }
}
export default MyhomeSearchRecovery