3. What software architecture are you using? e.g. Layered/How many layers? Repository? Something else?

I am using a 5 layered architecture with a UI layer, Service Layer, Entity Layer, Repository Layer, and the Database layer 
externally.

___
5. Please answer the following: discuss the pros/cons of the environments: VMs, docker, plain old computer. Things to consider: development vs production, working in teams, the OS you need, cost, licensing etc...


VMs: 
###  Pros:
    Alot of flexibility for OS, can also be used to standardize the environments for teams if done correctly. VMs can be hosted in
    the cloud which may cost money but is a good alternative if the team does not have enough resources for better hardware
    Alot of documentation with VMs as they have been around a long time, may be more helpful for smaller teams starting out

### Cons:
    Very Hardware dependent,
    VMS are independent of eachother so it has the downside of having to be individually managed by the team member, 
    if someone does not recieve a ciritcal fix, may impact work time
    

Docker:
###  Pros:
    Production environments can be made from the development environments much easier than the other two,
    Allows for multiple enviroments, and for the standardization of team development environment
    Very good for use in team pipelines to deploy to production and testing inbetween

### Cons:
    may be costly for large scale teams since liscences are required at scale, however for smaller teams can be a very good
    opportunity
    May be some security risks if wrong people are allowed access due to leaks or permission setup
    May have a steep learning curve to getting started and going into more advanced features


Computer: 
###  Pros:
    Compared to docker or VMs everything is on the hardware infront of you, so it can be more accessible for some people
    also, relatively low cost for small scale projects. 
    For liscencing its very easy aswell since most liscences can be individual
    
### Cons: 
    Environment between production and deployment can be very different, which is a gap that has to be
    thought about when developing
    For the OS you are limited to the one installed and cannot develop on another without the introduction of a VM or similar

    
