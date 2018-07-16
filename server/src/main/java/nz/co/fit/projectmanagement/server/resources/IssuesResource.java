package nz.co.fit.projectmanagement.server.resources;

import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.api.Issue;
import nz.co.fit.projectmanagement.server.core.HistoryService;
import nz.co.fit.projectmanagement.server.core.IssueService;
import nz.co.fit.projectmanagement.server.dao.entities.IssueModel;

@Path("/issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class IssuesResource extends CRUDLResource<Issue, IssueModel> {

	@Inject
	public IssuesResource(final IssueService componentService, final HistoryService historyService) {
		super(componentService, historyService);
	}

	@POST
	@DenyAll
	@Override
	public Issue create(final Issue value) throws ResourceException {
		// components should be created via the project resource
		return null;
	}

	@GET
	@DenyAll
	@Override
	public List<BaseIdable> list() throws ResourceException {
		// components should be listed via the project resource
		return null;
	}
}
