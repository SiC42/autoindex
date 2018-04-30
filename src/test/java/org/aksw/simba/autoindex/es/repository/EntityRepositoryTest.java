package org.aksw.simba.autoindex.es.repository;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.autoindex.datasource.sparql.SparqlHandler;
import org.aksw.simba.autoindex.es.model.DataClass;
import org.aksw.simba.autoindex.es.model.Entity;
import org.aksw.simba.autoindex.es.model.Property;
import org.aksw.simba.autoindex.request.Request;
import org.aksw.simba.autoindex.request.SearchRequest;
import org.aksw.simba.autoindex.request.SearchRequest.Category;
import org.aksw.simba.autoindex.request.SearchRequest.Type;
import org.aksw.simba.autoindex.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@RunWith(MockitoJUnitRunner.class)

public class EntityRepositoryTest{
	
	@Mock 
	private SparqlHandler sparqlHandler = Mockito.mock(SparqlHandler.class);
	
	@Mock
	private ElasticSearchRepositoryInterface elasticSearchRepositoryInterface = Mockito.mock(ElasticSearchRepositoryInterface.class);
	
	
	@Mock
	private Page<Entity> pageEntityspy = Mockito.mock(PageImpl.class);
	
	@InjectMocks
	public EntityRepository entityRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		//entityRepository = new EntityRepository();
	}
	
	@Test
	public void testAddHead() {
		Response response = new Response();
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		response = entityRepository.addHead(response);
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
	}
	public Entity createEntity(String label, String uri) {
		Entity entity = new Entity();
		entity.setLabel(label);
		entity.setUrl(uri);
		return entity;
	}
	public Property createProperty(String label, String uri) {
		Property entity = new Property();
		entity.setLabel(label);
		entity.setUrl(uri);
		return entity;
	}
	public DataClass createClass(String label, String uri) {
		DataClass entity = new DataClass();
		entity.setLabel(label);
		entity.setUrl(uri);
		return entity;
	}
	@Test
	public void testcreateResponse() {
		List<Entity> entityList = new ArrayList<Entity>();		
		entityList.add(createEntity("Test1" , "test1@test.com"));
		entityList.add(createEntity("Test2" , "test2@test.com"));
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		Response response = entityRepository.createResponse(entityList);
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
		assertTrue(response.getResults()!= null);
		assertTrue(response.getResults().getBindings() != null);
		assertTrue(response.getResults().getBindings().get(0) != null);
		assertTrue(response.getResults().getBindings().get(0).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(1).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(0).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(1).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(0).getLabel().getValue().equals("Test1"));
		assertTrue(response.getResults().getBindings().get(1).getLabel().getValue().equals("Test2"));
		assertTrue(response.getResults().getBindings().get(0).getUri().getValue().equals("test1@test.com"));
		assertTrue(response.getResults().getBindings().get(1).getUri().getValue().equals("test2@test.com"));
	}
	
	@Test
	public void testGetCategory() {
		Category category = Category.CLASS;
		String strCategory = entityRepository.getCategory(category);
		assertTrue(strCategory.equals("class"));
		category = Category.PROPERTY;
		strCategory = entityRepository.getCategory(category);
		assertTrue(strCategory.equals("property"));
		category = Category.ENTITY;
		strCategory = entityRepository.getCategory(category);
		assertTrue(strCategory.equals("entity"));
		category = Category.ALL;
		strCategory = entityRepository.getCategory(category);
		assertTrue(strCategory.equals(""));
	}
	
	@Test
	public void testGetType() {
		Type type = Type.LABEL;
		String strType = entityRepository.getType(type);
		assertTrue(strType.equals("label"));
		type = Type.URI;
		strType = entityRepository.getType(type);
		assertTrue(strType.equals("url"));
		type = Type.NONE;
		strType = entityRepository.getType(type);
		assertTrue(strType.equals("_all"));
	}
	
	@Test
	public void testSearch() {
		SearchRequest searchRequest = new SearchRequest();
		List<Entity> entityList = new ArrayList<Entity>();
		entityList.add(createEntity("Barack Obama" , "http://http://dbpedia.org/page/Barack_Obama"));
		entityList.add(createEntity("Barack" , "http://http://dbpedia.org/page/Barack"));
		searchRequest.setQuery("Obama");
		searchRequest.setCategory("entity");
		searchRequest.setType("label");
		Mockito.doReturn(pageEntityspy).when(elasticSearchRepositoryInterface).search(Mockito.any(SearchQuery.class));
		Mockito.doReturn(entityList).when(pageEntityspy).getContent();
		Response response = entityRepository.search(searchRequest);
		List<String> contentList = new ArrayList<String>();
		contentList.add("label");
		contentList.add("uri");
		assertTrue(response.getHead() != null);
		assertTrue(response.getHead().getVars().size() == 2);
		assertTrue(contentList.contains(response.getHead().getVars().get(0)));
		assertTrue(contentList.contains(response.getHead().getVars().get(1)));
		assertTrue(response.getResults()!= null);
		assertTrue(response.getResults().getBindings() != null);
		assertTrue(response.getResults().getBindings().get(0) != null);
		assertTrue(response.getResults().getBindings().get(0).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(1).getLabel()!=null);
		assertTrue(response.getResults().getBindings().get(0).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(1).getUri()!=null);
		assertTrue(response.getResults().getBindings().get(0).getLabel().getValue().equals("Barack Obama"));
		assertTrue(response.getResults().getBindings().get(1).getLabel().getValue().equals("Barack"));
		assertTrue(response.getResults().getBindings().get(0).getUri().getValue().equals("http://http://dbpedia.org/page/Barack_Obama"));
		assertTrue(response.getResults().getBindings().get(1).getUri().getValue().equals("http://http://dbpedia.org/page/Barack"));
		
	}
	
	@Test
	public void testCreateIndexWithEndPointURL() throws IOException {
		//Add Fake entities to index
		ArrayList<Entity> entityList = new ArrayList<Entity>();		
		entityList.add(createEntity("Test1" , "test1@test.com"));
		entityList.add(createEntity("Test2" , "test2@test.com"));
		ArrayList<Property> propertyList = new ArrayList<Property>();
		propertyList.add(createProperty("Property1" , "property1@test.com"));
		propertyList.add(createProperty("Property2" , "property2@test.com"));
		ArrayList<DataClass> classList = new ArrayList<DataClass>();
		classList.add(createClass("Class1" , "class1@test.com"));
		classList.add(createClass("Class2" , "class2@test.com"));
		Request request = new Request();
		request.setRequestType("URI");
		request.setUrl("http://dbpedia.org/sparql");
		request.setUseLocalDataSource(false);
		request.setUserId("0000001");
		Mockito.doReturn(entityList).when(sparqlHandler).fetchFromSparqlEndPoint(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(entityList);
		Mockito.doReturn(propertyList).when(sparqlHandler).fetchProperties(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(propertyList);
		Mockito.doReturn(classList).when(sparqlHandler).fetchClasses(request);
		Mockito.doReturn(null).when(elasticSearchRepositoryInterface).save(classList);
		Response response = entityRepository.createIndex(request);
		assertTrue(response != null);
		assertTrue(response.getBoolean()== true);
		
		
	}
}