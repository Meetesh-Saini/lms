class RequestHandledException extends Error {
    constructor(message: string) {
        super(message);
        this.name = this.constructor.name;
    }
}

class CallerHandledException extends Error {
    public resource;
    constructor(resource: any) {
        super();
        this.name = this.constructor.name;
        this.resource = resource;
    }
}


export { RequestHandledException, CallerHandledException }