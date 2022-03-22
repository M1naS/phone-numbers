import * as React from 'react';
import PropTypes from 'prop-types';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import ClearIcon from '@mui/icons-material/Clear';
import SearchIcon from '@mui/icons-material/Search';


const SearchBar = (props) => {
  return (
      <Box
          sx={{
            p: 0.5,
            pb: 0,
          }}
      >
        <TextField
            variant="standard"
            value={props.value}
            onChange={props.onChange}
            placeholder="Search…"
            InputProps={{
              startAdornment: <SearchIcon fontSize="small" />,
              endAdornment: (
                  <IconButton
                      title="Clear"
                      aria-label="Clear"
                      size="small"
                      style={{ visibility: props.value ? 'visible' : 'hidden' }}
                      onClick={props.clearSearch}
                  >
                    <ClearIcon fontSize="small" />
                  </IconButton>
              ),
            }}
        />
      </Box>
  );
}

SearchBar.propTypes = {
  clearSearch: PropTypes.func.isRequired,
  onChange: PropTypes.func.isRequired,
  value: PropTypes.string.isRequired,
};

export default SearchBar;
