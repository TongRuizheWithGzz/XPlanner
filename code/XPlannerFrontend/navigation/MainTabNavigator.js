import React from "react";
import { View, Text } from "react-native";
import {
  createStackNavigator,
  createBottomTabNavigator
} from "react-navigation";

import SchedularScreen from "../screens/SchedularScreen";
import StoreScreen from "../screens/StoreScreen";
import SettingsScreen from "../screens/SettingsScreen";

const HeaderStyles = {
  initialRouteName: "Home",
  navigationOptions: {
    headerStyle: {
      backgroundColor: "#f4511e"
    },
    headerTintColor: "#fff",
    headerTitleStyle: {
      fontWeight: "bold"
    }
  }
};

const SchedularStack = createStackNavigator({
  Schedular: SchedularScreen
});

SchedularStack.navigationOptions = {
  tabBarLabel: "Schedular",
  tabBarIcon: ({ focused }) => (
    <View>
      <Text>Schedular</Text>
    </View>
  )
};

const StoreStack = createStackNavigator({
  Store: StoreScreen
});

StoreStack.navigationOptions = {
  tabBarLabel: "Store",
  tabBarIcon: ({ focused }) => (
    <View>
      <Text>Store</Text>
    </View>
  )
};

const SettingsStack = createStackNavigator({
  Settings: SettingsScreen
});

SettingsStack.navigationOptions = {
  tabBarLabel: "Settings",
  tabBarIcon: ({ focused }) => (
    <View>
      <Text>Settings</Text>
    </View>
  )
};

export default createBottomTabNavigator({
  SchedularStack,
  StoreStack,
  SettingsStack
});
