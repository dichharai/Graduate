import React, {Component} from "react"; 

import Home from "./Home";
import Login from "./Login";
import GymStats from "./GymStats"; 
import Welcome from "./Welcome";  
import AuthLoading from "./AuthLoading";
import DetailedGymInfo from "./DetailedGymInfo"; 
import Signup from './Signup';
import CodeScanner from './CodeScanner';
import RecOptions from './RecOptions';
import RecAreas from './RecAreas';
import AreaEquip from './AreaEquip';
import EquipEntry from './EquipEntry';

import {StackNavigator, SwitchNavigator} from "react-navigation"; 

const AppStack = StackNavigator({ 
    Home: {screen: Home}, 
    Login: {screen: Login}, 
    GymStats: {screen: GymStats},
    DetailedGymInfo: {screen: DetailedGymInfo},
    CodeScanner: {screen: CodeScanner},
    Signup: {screen: Signup},
}); 

const AuthStack = StackNavigator({
    Welcome: {screen: Welcome},
    RecOptions: {screen: RecOptions},
    RecAreas: {screen: RecAreas},
    AreaEquip: {screen: AreaEquip},
    EquipEntry: {screen: EquipEntry}
}); 
// wrapping AppStack & AuthStack in SwitchNavigator so that when a user logs in they aren't shown previously logged in fill-in-page in navigation. 

export default SwitchNavigator(
    {
        AuthLoading: AuthLoading, 
        App: AppStack, 
        Auth: AuthStack, 
    }, 
    {
        initialRouteName: "AuthLoading", 
    }
); 
/*
const RootStack = StackNavigator(
    {
        Home: {screen: Home}, 
        Login: {screen: Login}, 
        GymStats: {screen: GymStats},
        Welcome: {screen: Welcome},  
    },
);
class HerkNav extends Component{
    render(){
        return <RootStack/>
    }
}
export default HerkNav; 
*/
