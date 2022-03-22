import React from 'react';
import {DataGrid} from '@mui/x-data-grid';

import axios from 'axios'
import {Container} from "@mui/material";
import SearchBar from "./components/SearchBar";

import './App.css';

class DataTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      rows: [],
      rowCount: 0,
      search: "",
      pageNumber: 0,
      pageSize: 10,
      sortColumn: "country",
      sortDesc: false,
      loading: true,
    };
  }

  async getItemsFromServer() {
    this.setState({
      ...this.state,
      loading: true,
    }, async () => {
      await axios.get(`http://localhost:8080/api/country/read/list?page=${this.state.pageNumber}&size=${this.state.pageSize}&sortBy=${this.state.sortColumn}&sortDesc=${this.state.sortDesc}&search=${this.state.search}`).then(response => {
        if (response.data.data.length > 0) {
          this.setState({
            ...this.state,
            loading: false,
            rows: response.data.data,
            rowCount: response.data.metaData.totalCount
          });
        } else {
          this.setState({
            ...this.state,
            loading: false,
            rows: [],
            rowCount: 0
          });
          console.error(response.data.metaData?.error);
        }
      }).catch(error => {
        console.error(error);
        this.setState({
          ...this.state,
          loading: false,
        });
      });
    });
  }

  async componentDidMount() {
    await this.getItemsFromServer();
  }

  render() {
    const columns = [
      {field: 'country', headerName: 'Country', flex: 2},
      {field: 'code', headerName: 'Code', flex: 1},
      {field: 'phone', headerName: 'Phone number', flex: 2},
      {field: 'state', headerName: 'State', flex: 1},
    ];

    return (
        <div>
          <Container maxWidth={false}>
            <h3>Country Phone numbers</h3>
            <div style={{height: '100%'}}>
              <DataGrid
                  autoHeight
                  components={{ Toolbar: SearchBar }}
                  componentsProps={{
                    toolbar: {
                      value: this.state.search,
                      onChange: async (event) => {
                        this.setState({
                          ...this.state,
                          search: event.target.value
                        }, async () => await this.getItemsFromServer());
                      },
                      clearSearch: async () => {
                        this.setState({
                          ...this.state,
                          search: ""
                        }, async () => await this.getItemsFromServer());
                      },
                    },
                  }}
                  loading={this.state.loading}
                  rows={this.state.rows}
                  columns={columns}
                  sortingOrder={["asc", "desc"]}
                  pageSize={this.state.pageSize}
                  rowsPerPageOptions={[5, 10, 15]}
                  paginationMode={"server"}
                  rowCount={this.state.rowCount}
                  onPageChange={async (pageNumber) => {
                      this.setState({
                        ...this.state,
                        pageNumber: pageNumber
                      }, async () => await this.getItemsFromServer());
                  }}
                  onPageSizeChange={async (pageSize) => {
                    this.setState({
                      ...this.state,
                      pageSize: pageSize
                    }, async () => await this.getItemsFromServer());
                  }}
                  sortingMode={"server"}
                  onSortModelChange={async (model) => {
                    this.setState({
                      ...this.state,
                      pageNumber: 0,
                      sortColumn: model[0].field,
                      sortDesc: (model[0].sort === "desc"),
                    }, async () => await this.getItemsFromServer());
                  }}
                  pagination
                  disableColumnMenu
                  disableColumnSelector
                  disableDensitySelector
                  disableExtendRowFullWidth
                  disableSelectionOnClick
              />
              </div>
          </Container>
        </div>
    );
  }
}

export default DataTable;