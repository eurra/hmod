
package optefx.loader;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Enrique Urra C.
 */
public class LoaderTests
{    
    public static class Loaders
    {
        @LoadsComponent
        public static void nonDeclaredProviding(ComponentRegister cr)
        {
            cr.provide(new EntityA());
        }

        @LoadsComponent({ Entity.class, ComplexEntity.class })
        public static void missingProviding(ComponentRegister cr)
        {
            cr.provide(new EntityA(), Entity.class);
        }
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    /*************** ENTITY ***************/
    
    public interface Entity
    {
        String getName();
    }
    
    public interface Taggable
    {
        void setName(String name);
    }
    
    public static class EntityA implements Entity
    {
        @LoadsComponent(EntityA.class)
        public static void load(ComponentRegister cr)
        {
            cr.provide(new EntityA());
        }
        
        @Override
        public String getName()
        {
            return "entity A";
        }
    }
    
    public static class EntityB implements Entity
    {
        @LoadsComponent(Entity.class)
        public static void load(ComponentRegister cr)
        {
            cr.provide(new EntityB(), Entity.class);
        }
        
        @Override
        public String getName()
        {
            return "entity B";
        }
    }
    
    public static class CyclicEntityA implements Entity
    {
        @LoadsComponent(CyclicEntityA.class)
        public static void load(ComponentRegister cr, Entity e)
        {
            cr.provide(new CyclicEntityA(e));
        }
        
        private final Entity entity;

        public CyclicEntityA(Entity entity)
        {
            this.entity = entity;
        }
        
        @Override
        public String getName()
        {
            return entity.getName();
        }
    }
    
    public static class CyclicExtEntityA extends CyclicEntityA
    {
        @LoadsComponent(CyclicExtEntityA.class)
        public static void load(ComponentRegister cr, CyclicEntityB ceB)
        {
            cr.provide(new CyclicExtEntityA(ceB));
        }
        
        public CyclicExtEntityA(CyclicEntityB entity)
        {
            super(entity);
        }
    }
    
    public static class CyclicEntityB implements Entity
    {
        @LoadsComponent(CyclicEntityB.class)
        public static void load(ComponentRegister cr, CyclicEntityA ceA)
        {
            cr.provide(new CyclicEntityB(ceA));
        }
        
        private final Entity entity;

        public CyclicEntityB(Entity entity)
        {
            this.entity = entity;
        }
        
        @Override
        public String getName()
        {
            return entity.getName();
        }
    }
    
    public static class CyclicEntity implements Entity
    {
        @LoadsComponent(CyclicEntity.class)
        public static void load(ComponentRegister cr, CyclicComplexEntityB ceB)
        {
            cr.provide(new CyclicEntity(ceB));
        }
        
        private final CyclicComplexEntityB entity;

        public CyclicEntity(CyclicComplexEntityB entity)
        {
            this.entity = entity;
        }
        
        @Override
        public String getName()
        {
            return entity.getText();
        }
    }
    
    public static class ParametrizedEntity implements Entity
    {
        public static final Parameter<String> TEXT = new Parameter<>("ParametrizedEntity.TEXT");
        public static final Parameter<Integer> NUMBER = new Parameter<>("ParametrizedEntity.NUMBER");
        public static final Trigger<Integer> TRIGGABLE_NUMBER = new Trigger<>("ParametrizedEntity.T_NUMBER");
        
        @LoadsComponent(Entity.class)
        public static void load(ComponentRegister cr, ParameterRegister pr)
        {
            String text = pr.getValue(TEXT);
            Integer number = pr.getRequiredValue(NUMBER);
            
            ParametrizedEntity pe = cr.provide(new ParametrizedEntity(text, number), Entity.class);
            pr.addListener(TRIGGABLE_NUMBER, (val) -> pe.value = val);
        }
        
        private final String text;
        private int value;

        public ParametrizedEntity(String text, int value)
        {
            this.text = text;
            this.value = value;
        }
        
        @Override
        public String getName()
        {
            return "name '" + text + " " + value + "'";
        }
    }
    
    public static class ProcessableEntity implements Entity, Taggable
    {
        @LoadsComponent({ Entity.class, Taggable.class })
        public static void loadEntity(ComponentRegister cr)
        {
            cr.provide(new ProcessableEntity(), Entity.class, Taggable.class);
        }
        
        @LoadsComponent
        public static void loadValidation(Taggable t)
        {
            t.setName("prueba");
        }
        
        private String name;

        @Override
        public void setName(String name)
        {
            this.name = name;
        }
        
        @Override
        public String getName()
        {
            return name;
        }
        
        @Processable
        private void process()
        {
            if(name == null) 
                throw new ModuleLoadException("null name");
        }
    }
    
    /*************** COMPLEX ENTITY ***************/
    
    public interface ComplexEntity
    {
        String getText();
    }
    
    public static class SimpleComplexEntity implements ComplexEntity
    {
        @LoadsComponent(SimpleComplexEntity.class)
        public static void load(ComponentRegister cr, Entity e)
        {
            cr.provide(new SimpleComplexEntity(e), SimpleComplexEntity.class);
        }
        
        private final Entity entity;

        public SimpleComplexEntity(Entity entity)
        {
            this.entity = entity;
        }

        @Override
        public String getText()
        {
            return entity.getName();
        }
    }
    
    public static class CyclicComplexEntityA implements ComplexEntity
    {
        @LoadsComponent(CyclicComplexEntityA.class)
        public static void load(ComponentRegister cr, Entity e)
        {
            cr.provide(new CyclicComplexEntityA(e));
        }
        
        private final Entity entity;

        public CyclicComplexEntityA(Entity entity)
        {
            this.entity = entity;
        }

        @Override
        public String getText()
        {
            return entity.getName();
        }        
    }
    
    public static class CyclicComplexEntityB implements ComplexEntity
    {
        @LoadsComponent(CyclicComplexEntityB.class)
        public static void load(ComponentRegister cr, CyclicComplexEntityA ceA)
        {
            cr.provide(new CyclicComplexEntityB(ceA));
        }
        
        private final CyclicComplexEntityA entity;

        public CyclicComplexEntityB(CyclicComplexEntityA entity)
        {
            this.entity = entity;
        }

        @Override
        public String getText()
        {
            return entity.getText();
        }
    }
    
    /*************** BIG ENTITY ***************/
    
    public static class BigEntity
    {
        @LoadsComponent(BigEntity.class)
        public static void load(ComponentRegister cr, EntityA entityA, Entity entity, ComplexEntity complexEntity)
        {
            cr.provide(new BigEntity(entityA, entity, complexEntity));
        }
        
        private final EntityA entityA;
        private final Entity entity;
        private final ComplexEntity complexEntity;

        public BigEntity(EntityA entityA, Entity entity, ComplexEntity complexEntity)
        {
            this.entityA = entityA;
            this.entity = entity;
            this.complexEntity = complexEntity;
        }
        
        public String getTextA()
        {
            return entityA.getName();
        }
        
        public String getText()
        {
            return entity.getName();
        }
        
        public String getComplexText()
        {
            return complexEntity.getText();
        }
    }
    
    /*************** TESTS ***************/
    
    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void prev()
    {
        System.out.println();
    }
    
    @Test
    public void basic()
    {
        Module mA = new ModuleLoader().
            loadAll(EntityA.class, SimpleComplexEntity.class).
            getModule();
        
        Module mB = new ModuleLoader().
            loadAll(EntityB.class, SimpleComplexEntity.class).
            getModule();
        
        assertEquals("Entity A", mA.getInstance(ComplexEntity.class).getText(), "entity A");
        assertEquals("Entity B", mB.getInstance(ComplexEntity.class).getText(), "entity B");
        
        System.out.println(mA.getLoadLog());
        System.out.println(mB.getLoadLog());
    }
    
    @Test
    public void unmetDependency()
    {
        thrown.expect(UnmetDependencyException.class);
        
        new ModuleLoader().
            load(SimpleComplexEntity.class).
            getInstance(ComplexEntity.class);
    }
    
    @Test
    public void missingComponent()
    {
        thrown.expect(MissingComponentException.class);
        
        new ModuleLoader().
            load(EntityA.class).
            getInstance(ComplexEntity.class);
    }
    
    @Test
    public void ambiguous()
    {
        thrown.expect(AmbiguousImplementationException.class);
        
        new ModuleLoader().
            loadAll(SimpleComplexEntity.class, EntityA.class, EntityB.class).
            getInstance(Entity.class);
    }
    
    @Test
    public void nonDeclaredProviding()
    {
        thrown.expect(NonDeclaredProvidingException.class);
        
        new ModuleLoader().
            load(Loaders.class, "nonDeclaredProviding").
            getModule();
    }
    
    @Test
    public void missingProviding()
    {
        thrown.expect(MissingProvidingException.class);
        
        new ModuleLoader().
            load(Loaders.class, "missingProviding").
            getInstance(Entity.class);
    }
    
    @Test
    public void selfCyclicDependency()
    {
        thrown.expect(CyclicDependencyException.class);
        
        new ModuleLoader().
            loadAll(CyclicEntityA.class).
            getInstance(CyclicEntityA.class);
    }
    
    @Test
    public void directCyclic()
    {
        thrown.expect(CyclicDependencyException.class);
        
        new ModuleLoader().
            loadAll(CyclicExtEntityA.class, CyclicEntityB.class).
            getInstance(CyclicEntityB.class);
    }
    
    @Test
    public void closeButNotCyclic()
    {
        Module m = new ModuleLoader().
            loadAll(CyclicComplexEntityA.class, CyclicComplexEntityB.class, EntityB.class).
            getModule();
        
        CyclicComplexEntityA entity = m.getInstance(CyclicComplexEntityA.class);
        
        assertEquals("Cyclic Entity", entity.getText(), "entity B");
        System.out.println(m.getLoadLog());
    }
    
    @Test
    public void indirectCyclic()
    {
        thrown.expect(CyclicDependencyException.class);
        
        new ModuleLoader().
            loadAll(CyclicComplexEntityA.class, CyclicComplexEntityB.class, CyclicEntity.class).
            getInstance(CyclicComplexEntityA.class);
    }
    
    @Test
    public void bigEntity()
    {
        Module m = new ModuleLoader().
            loadAll(BigEntity.class, EntityA.class, SimpleComplexEntity.class).
            getModule();
        
        BigEntity be = m.getInstance(BigEntity.class);        
        assertEquals("Big Entity", be.getText(), "entity A");
        assertEquals("Big Entity", be.getTextA(), "entity A");
        assertEquals("Big Entity", be.getComplexText(), "entity A");
        
        System.out.println(m.getLoadLog());
    }
    
    @Test
    public void bigEntityMissing()
    {
       thrown.expect(UnmetDependencyException.class);
        
        new ModuleLoader().
            loadAll(BigEntity.class, EntityB.class, SimpleComplexEntity.class).
            getInstance(BigEntity.class);
    }
    
    @Test
    public void bigEntityAmbiguous()
    {
        thrown.expect(AmbiguousImplementationException.class);
        
        new ModuleLoader().
            loadAll(BigEntity.class, EntityA.class, EntityB.class, SimpleComplexEntity.class).
            getInstance(BigEntity.class);
    }
    
    @Test
    public void parameters()
    {
        ModuleLoader loader = new ModuleLoader().
            load(ParametrizedEntity.class).
            setParameter(ParametrizedEntity.TEXT, "hola").
            setParameter(ParametrizedEntity.NUMBER, 6);
        
        Entity entity1 = loader.getInstance(Entity.class);
        assertEquals("Parametrized valued entity 1", entity1.getName(), "name 'hola 6'");
        
        Entity entity2 = loader.clearParameter(ParametrizedEntity.TEXT).getInstance(Entity.class);
        assertEquals("Parametrized valued entity 2", entity2.getName(), "name 'null 6'");
        
        Module m = loader.getModule();
        m.setParameter(ParametrizedEntity.TRIGGABLE_NUMBER, 2);
        assertEquals("Parametrized valued entity 2", m.getInstance(Entity.class).getName(), "name 'null 2'");
    }
    
    @Test
    public void missingParameter()
    {
        thrown.expect(MissingParameterException.class);
        
        new ModuleLoader().
            load(ParametrizedEntity.class).
            setParameter(ParametrizedEntity.TEXT, "hola").
            getModule();
    }
    
    @Test
    public void validation()
    {
        Entity e = new ModuleLoader().
            load(ProcessableEntity.class).
            getInstance(Entity.class);
        
        assertEquals("Validable entity", e.getName(), "prueba");
    }
    
    @Test
    public void validationError()
    {
        thrown.expect(ModuleLoadException.class);
        
        new ModuleLoader().
            load(ProcessableEntity.class, "loadEntity").
            getInstance(Entity.class);
    }
}
