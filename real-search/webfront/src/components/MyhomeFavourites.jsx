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


class MyhomeFavourites extends Component {
    state = {
        favoriteCount: 0,
        favoriteResults: [],
    };


    favoritesMyhome = async (event) => {
        event.preventDefault();
        let response = await fetch('/myhomefavourites');
        let body = await response.json();
        this.setState({ favoriteCount: body.Results.length, favoriteResults: body.Results });
    }

    render() {
        return (
            <div class="tab">
                <button onClick={(event) => this.favoritesMyhome(event)}>Lookup favourites</button>
                <h2>Favourites Found {this.state.favoriteCount}</h2>
                <Table dataSource={this.state.favoriteResults} columns={columns} />
            </div>
        );
    }
}
export default MyhomeFavourites