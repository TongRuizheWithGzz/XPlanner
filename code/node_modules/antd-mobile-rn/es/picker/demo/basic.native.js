import _classCallCheck from 'babel-runtime/helpers/classCallCheck';
import _createClass from 'babel-runtime/helpers/createClass';
import _possibleConstructorReturn from 'babel-runtime/helpers/possibleConstructorReturn';
import _inherits from 'babel-runtime/helpers/inherits';
import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { List, Picker } from 'antd-mobile-rn';
import { district } from 'antd-mobile-demo-data';
var CustomChildren = function CustomChildren(props) {
    return React.createElement(
        TouchableOpacity,
        { onPress: props.onClick },
        React.createElement(
            View,
            { style: { height: 36, paddingLeft: 15, flexDirection: 'row', alignItems: 'center' } },
            React.createElement(
                Text,
                { style: { flex: 1 } },
                props.children
            ),
            React.createElement(
                Text,
                { style: { textAlign: 'right', color: '#888', marginRight: 15 } },
                props.extra
            )
        )
    );
};

var PopupExample = function (_React$Component) {
    _inherits(PopupExample, _React$Component);

    function PopupExample(props) {
        _classCallCheck(this, PopupExample);

        var _this = _possibleConstructorReturn(this, (PopupExample.__proto__ || Object.getPrototypeOf(PopupExample)).call(this, props));

        _this.onClick = function () {
            // console.log('start loading data');
            setTimeout(function () {
                _this.setState({
                    data: district
                });
            }, 500);
        };
        _this.onChange = function (value) {
            // console.log(value);
            _this.setState({ value: value });
        };
        _this.state = {
            data: [],
            value: [],
            pickerValue: []
        };
        return _this;
    }

    _createClass(PopupExample, [{
        key: 'render',
        value: function render() {
            var _this2 = this;

            return React.createElement(
                View,
                { style: { marginTop: 30 } },
                React.createElement(
                    List,
                    null,
                    React.createElement(
                        Picker,
                        { data: this.state.data, cols: 2, value: this.state.value, onChange: this.onChange },
                        React.createElement(
                            List.Item,
                            { arrow: 'horizontal', last: true, onClick: this.onClick },
                            '\u7701\u5E02\u9009\u62E9(\u5F02\u6B65\u52A0\u8F7D)'
                        )
                    ),
                    React.createElement(
                        Picker,
                        { title: '\u9009\u62E9\u5730\u533A', data: district, cols: 2, value: this.state.pickerValue, onChange: function onChange(v) {
                                return _this2.setState({ pickerValue: v });
                            }, onOk: function onOk(v) {
                                return _this2.setState({ pickerValue: v });
                            } },
                        React.createElement(
                            CustomChildren,
                            null,
                            'Customized children'
                        )
                    )
                )
            );
        }
    }]);

    return PopupExample;
}(React.Component);

export default PopupExample;