package io.redspark.ireadme.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import io.redspark.ireadme.dto.TeamDTO;
import io.redspark.ireadme.entity.Team;
import io.redspark.ireadme.entity.User;
import io.redspark.ireadme.form.GenericForm;
import io.redspark.ireadme.security.IReadmeSecurityContext;
import io.redspark.ireadme.service.IReadmeService;
import io.redspark.ireadme.service.TeamService;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

	@Autowired
	private IReadmeService service;
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{id}", method = GET)
	public TeamDTO get(@PathVariable("id") Long id) {
		
		Team team = service.getTeamService().findTeamById(id);
		
		return new TeamDTO(team);
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/me" , method = GET)
	public Collection<TeamDTO> list() {
	
		User user = IReadmeSecurityContext.getLoggedUser();
		
		return user.getTeams()
					.stream()
					.map(TeamDTO::new)
					.collect(Collectors.toList());
	}
	
	@ResponseStatus(CREATED)
	@RequestMapping(method = POST)
	public TeamDTO create(@Valid @ModelAttribute GenericForm form){
		
		Team team = form.toTeamEntity();
		team.getUsers().add(IReadmeSecurityContext.getLoggedUser());
		
		team = service.getTeamService().getRepository().save(team);
		
		return new TeamDTO(team);
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{id}", method = PUT)
	public TeamDTO update(@PathVariable("id") Long id, @ModelAttribute GenericForm form) {
		
		Team team = service.getTeamService().findTeamById(id);
		
		team.setName(form.getName());
		team.setImage(form.getImage());
		team.setDescription(form.getDescription());
		
		return new TeamDTO(team);
	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = "/{id}/member", method = PUT)
	public TeamDTO addMember(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
		
		Team team = service.getTeamService().findTeamById(id);
		User user = service.getUserService().findUserById(userId);
		
		team.getUsers().add(user);
		service.getTeamService().getRepository().save(team);
		
		return new TeamDTO(team);
	}
	
	@Transactional
	@RequestMapping(value = "/{id}", method = DELETE)
	public TeamDTO delete(@PathVariable("id") Long id) {
		
		TeamService teamService = service.getTeamService();
		
		Team team = teamService.findTeamById(id);
		teamService.getRepository().delete(team);
		
		return new TeamDTO(team);
	}
	
}
