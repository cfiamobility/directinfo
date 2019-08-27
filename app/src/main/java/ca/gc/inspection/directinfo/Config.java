package ca.gc.inspection.directinfo;

class Config {

    /** IP_ADDRESS is the ip of where the middle tier is running. IE. 10.0.2.2 is "localhost" for
     *  mobile emulators, which connects to the middle tier (node) running on the local machine.
     *  This is heavily used for testing.
     *
     *  The other address is for the virtual box where the middle tier and database are sitting, which
     *  is used for production. This server is currently down and we are still in the progress of
     *  creating a new one to serve as our database.
     */
    static String IP_ADDRESS = "http://10.0.2.2:3000/";
    //static String IP_ADDRESS = "http://13.88.234.89:3000/";
}
