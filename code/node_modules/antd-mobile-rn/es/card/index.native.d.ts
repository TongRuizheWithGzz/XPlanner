/// <reference types="react" />
import React from 'react';
import { StyleProp, ViewStyle } from 'react-native';
import CardBody from './CardBody.native';
import CardFooter from './CardFooter.native';
import CardHeader from './CardHeader.native';
import { CardPropsType } from './PropsType';
import { ICardStyle } from './style/index.native';
export interface CardNativeProps extends CardPropsType {
    styles?: ICardStyle;
    style?: StyleProp<ViewStyle>;
}
export default class Card extends React.Component<CardNativeProps, any> {
    static defaultProps: {
        style: {};
        full: boolean;
        styles: any;
    };
    static Header: typeof CardHeader;
    static Body: typeof CardBody;
    static Footer: typeof CardFooter;
    render(): JSX.Element;
}
