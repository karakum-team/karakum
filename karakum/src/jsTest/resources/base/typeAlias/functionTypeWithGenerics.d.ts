interface Response {}

type JsonFunction = <Data>(data: Data, init?: {}) => Response;
