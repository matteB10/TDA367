package com.masthuggis.boki.presenter;

public class AdvertsPresenterTest {
/*
    private AdvertsPresenter presenter;
    private AdvertsPresenterView view;
    private boolean onCreateHeaderCalled;
    private boolean onCreateNoResultsFoundCalled;
    @Mock private DataModel databaseMock;
    private List<Advertisement> testData;

    @Before
    public void before() {
        onCreateHeaderCalled = false;
        onCreateNoResultsFoundCalled = false;
        presenter = new MockPresenter(new MockView(), DependencyInjector.injectDataModel());

        Mockito.doNothing().when(databaseMock).

        testData = new ArrayList<>();
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
        testData.add(AdFactory.createAd("190101200000", "UniqueOwnerID", "UniqueAdID", "Title","", 300, Advert.Condition.GOOD,"", new ArrayList<>(),null));
    }

    @Test
    public void testNumAdvertsIsCorrect() {
        assertEquals(presenter.getItemCount(), testData.size());
    }


    class MockPresenter extends AdvertsPresenter {
        public MockPresenter(AdvertsPresenterView view, DataModel dataModel) {
            super(view, dataModel);
        }

        @Override
        public List<Advertisement> getData() {
            return testData;
        }

        @Override
        public List<Advertisement> sort(List<Advertisement> adverts) {
            return SortManager.getInstance().sortWithDefaultSorting(adverts);
        }
    }

    class MockView extends AdvertsView {

        @Override
        protected View onCreateHeaderLayout() {
            onCreateHeaderCalled = true;
            return null;
        }

        @Override
        protected View onCreateNoResultsFoundLayout() {
            onCreateNoResultsFoundCalled = true;
            return null;
        }

        @Override
        protected AdvertsPresenter getPresenter() {
            return null;
        }
    }*/

}
