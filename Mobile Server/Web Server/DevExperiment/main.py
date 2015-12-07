import webapp2
import logging

class handleSingleRequest(webapp2.RequestHandler):
    def post(self):
        body = self.request.get('body')
        sender = self.request.get('sender')
        # apply your logic and use any sms gateway to response to sender
        logging.info("send a response to "+ str(sender))
        
class handleBatchRequest(webapp2.RequestHandler):
    def post(self):
        batch = self.request.get('batch')
        logging.info("handler request for all sender in batch :"+ str(batch))
        

app = webapp2.WSGIApplication([('/post1/batch', handleBatchRequest),
                               ('/post1/singleton', handleSingleRequest)                  
                               ], debug=True)                   
        