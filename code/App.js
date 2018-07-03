import React, {
    Component
} from 'react'

import {
    View,
    Text,
    TouchableOpacity,
    Platform,
    Image,
    StyleSheet
} from 'react-native'

import NavigationBar from 'react-native-navbar'
import TabNavigator from 'react-native-tab-navigator'

const styles = {
    navbar: {
        alignItems: 'center',
        borderColor: '#e1e1e1',
        borderBottomWidth: 1
    },
    title: {
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: 5
    },
    titleText: {
        fontSize: 18
    },
    button: {
        width: 35,
        alignItems: 'center',
        justifyContent: 'center'
    },
    buttonText: {
        fontSize: 16,
        color: '#333'
    },
    buttonIconFontText: {
        fontSize: 26,
        fontFamily: 'iconfont'
    }
}
const tab1 = {
key: 'apartment',
    title: '公寓',
    img: require('../images/icon_tabbar_1.png'),
    img_on: require('../images/icon_tabbar_1_on.png'),
};

const tab2 = {
    key: 'service',
    title: '老人服务',
    img: require('../images/icon_tabbar_2.png'),
    img_on: require('../images/icon_tabbar_2_on.png'),
};


const tab3 = {
    key: 'customer',
    title: '客服咨询',
    img: require('../images/icon_tabbar_3.png'),
    img_on: require('../images/icon_tabbar_3_on.png'),
};


const tab4 = {
    key: 'profile',
    title: '我的',
    img: require('../images/icon_tabbar_4.png'),
    img_on: require('../images/icon_tabbar_4_on.png'),
};


export default class HomePage extends Component {
    constructor(props) {
        super(props);
        this.state = {selectedTab: tab1.key}
    }

    _renderTabItem(tabJson, Comp) {

        return (
            <TabNavigator.Item
                selectedTitleStyle={styles.tabTitleStyle}
                title={tabJson.title}
                selected={this.state.selectedTab === tabJson.key}
                renderIcon={() => <Image style={styles.tabIcon} source={tabJson.img}/>}
                renderSelectedIcon={() => <Image style={styles.tabIcon} source={tabJson.img_on}/>}
                onPress={() => this.setState({selectedTab: tabJson.key})}>
                {Comp}
            </TabNavigator.Item>
        );
    }


    render() {
        return (
            <View style={{flex: 1}}>
                <TabNavigator hidesTabTouch={true} tabBarStyle={styles.tab}>
                    {this._renderTabItem(tab1, "page1")}
                    {this._renderTabItem(tab2, "page2")}
                    {this._renderTabItem(tab3, "page3")}
                    {this._renderTabItem(tab4, "page4")}
                </TabNavigator>
            </View >
        );
    }
}


const styles = StyleSheet.create({
    tab: {
        height: 52,
        backgroundColor: '#F7F7FA',
        alignItems: 'center',
    },
    tabIcon: {
        width: 30,
        height: 30,
        resizeMode: 'stretch',
        marginTop: 12.5
    },
    tabTitleStyle: {
        color: '#e75404'
    },
});